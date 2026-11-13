
const SONAR_URL = process.env.SONAR_HOST_URL;
const SONAR_TOKEN = process.env.SONAR_TOKEN;
const PROJECT_KEY = process.env.SONAR_PROJECT_KEY;

const [owner, repo] = process.env.GITHUB_REPOSITORY.split("/");
const GITHUB_TOKEN = process.env.GITHUB_TOKEN;

const API = "https://api.github.com";
const LABEL = "sonarqube";

const sleep = ms =>
  new Promise(resolve => setTimeout(resolve, ms));

// =====================================
// GITHUB API CON CONTROL DE RATE LIMIT
// =====================================

async function github(url, options = {}) {
  const maxRetries = 5;

  for (let attempt = 0; attempt <= maxRetries; attempt++) {
    const response = await fetch(url, {
      ...options,
      headers: {
        Accept: "application/vnd.github+json",
        "X-GitHub-Api-Version": "2022-11-28",
        Authorization: `Bearer ${GITHUB_TOKEN}`,
        ...options.headers
      }
    });

    if (response.ok) {
      if (response.status === 204) return null;
      return response.json();
    }

    const errorBody = await response.text();

    const isRateLimit =
      [403, 429].includes(response.status) &&
      (
        /rate limit|temporarily blocked|abuse detection/i.test(errorBody) ||
        response.headers.get("x-ratelimit-remaining") === "0"
      );

    if (!isRateLimit || attempt === maxRetries) {
      throw new Error(
        `GitHub error ${response.status}: ${errorBody}`
      );
    }

    const retryAfter = response.headers.get("retry-after");
    const remaining = response.headers.get("x-ratelimit-remaining");
    const reset = response.headers.get("x-ratelimit-reset");

    let waitMs;

    if (retryAfter) {
      const seconds = Number(retryAfter);

      waitMs = Number.isFinite(seconds)
        ? seconds * 1000
        : Math.max(Date.parse(retryAfter) - Date.now(), 1000);

    } else if (remaining === "0" && reset) {
      waitMs = Math.max(
        Number(reset) * 1000 - Date.now(),
        1000
      );

    } else {
      waitMs = 60_000 * (2 ** attempt);
    }

    console.log(
      `Límite de GitHub. Esperando ${Math.ceil(waitMs / 1000)} segundos...`
    );

    await sleep(waitMs);
  }
}

// =====================================
// CONSULTAR SONARCLOUD
// =====================================

async function getSonarIssues(resolved) {
  const auth = Buffer.from(`${SONAR_TOKEN}:`).toString("base64");

  const issues = [];

  for (let page = 1; ; page++) {
    const params = new URLSearchParams({
      componentKeys: PROJECT_KEY,
      resolved: String(resolved),
      ps: "100",
      p: String(page)
    });

    const response = await fetch(
      `${SONAR_URL}/api/issues/search?${params}`,
      {
        headers: {
          Authorization: `Basic ${auth}`
        }
      }
    );

    if (!response.ok) {
      throw new Error(
        `SonarCloud error ${response.status}: ${await response.text()}`
      );
    }

    const data = await response.json();

    issues.push(...data.issues);

    console.log(
      `SonarCloud (${resolved ? "resueltos" : "abiertos"}) página ${page}: ${data.issues.length}`
    );

    if (
      issues.length >= data.total ||
      data.issues.length === 0
    ) {
      break;
    }
  }

  return issues;
}

// =====================================
// CONSULTAR TICKETS DE GITHUB
// =====================================

async function getExistingTickets() {
  const tickets = [];

  for (let page = 1; ; page++) {
    const params = new URLSearchParams({
      state: "all",
      labels: LABEL,
      per_page: "100",
      page: String(page)
    });

    const data = await github(
      `${API}/repos/${owner}/${repo}/issues?${params}`
    );

    tickets.push(
      ...data.filter(ticket => !ticket.pull_request)
    );

    if (data.length < 100) break;
  }

  return tickets;
}

// =====================================
// OBTENER CLAVE SONAR DE UN TICKET
// =====================================

function getSonarKey(ticket) {
  const match = ticket.body?.match(
    /<!-- sonarcloud-issue-key: ([^ ]+) -->/
  );

  return match ? match[1] : null;
}

// =====================================
// CREAR TICKET
// =====================================

async function createIssue(issue) {
  const file = issue.component.replace(
    `${PROJECT_KEY}:`,
    ""
  );

  const sonarLink =
    `${SONAR_URL}/project/issues?id=${encodeURIComponent(PROJECT_KEY)}` +
    `&open=${encodeURIComponent(issue.key)}`;

  const title =
    `[SonarQube] [${issue.severity}] ${issue.message}`
      .slice(0, 256);

  const body = `
## Hallazgo detectado por SonarCloud

- **Severidad:** ${issue.severity}
- **Tipo:** ${issue.type}
- **Regla:** ${issue.rule}
- **Archivo:** ${file}
- **Línea:** ${issue.line ?? "No especificada"}

### Descripción

${issue.message}

### Referencia

[Ver issue en SonarCloud](${sonarLink})

<!-- sonarcloud-issue-key: ${issue.key} -->
`;

  await github(
    `${API}/repos/${owner}/${repo}/issues`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        title,
        body,
        labels: [LABEL]
      })
    }
  );

  console.log(`Ticket creado: ${issue.key}`);

  await sleep(2000);
}

// =====================================
// CERRAR TICKET
// =====================================

async function closeIssue(ticket) {
  if (ticket.state === "closed") return;

  await github(
    `${API}/repos/${owner}/${repo}/issues/${ticket.number}`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        state: "closed",
        state_reason: "completed"
      })
    }
  );

  console.log(
    `Ticket cerrado: #${ticket.number} - ${getSonarKey(ticket)}`
  );

  await sleep(2000);
}

// =====================================
// REABRIR TICKET
// =====================================

async function reopenIssue(ticket) {
  if (ticket.state === "open") return;

  await github(
    `${API}/repos/${owner}/${repo}/issues/${ticket.number}`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        state: "open"
      })
    }
  );

  console.log(
    `Ticket reabierto: #${ticket.number} - ${getSonarKey(ticket)}`
  );

  await sleep(2000);
}

// =====================================
// SINCRONIZACIÓN PRINCIPAL
// =====================================

async function main() {
  console.log("======================================");
  console.log(" SONARCLOUD -> GITHUB ISSUES");
  console.log(" SINCRONIZACIÓN COMPLETA");
  console.log("======================================");

  // Obtener ambos estados de SonarCloud
  const openIssues = await getSonarIssues(false);
  const resolvedIssues = await getSonarIssues(true);

  // Obtener tickets actuales de GitHub
  const githubTickets = await getExistingTickets();

  // Crear mapas de claves
  const openMap = new Map(
    openIssues.map(issue => [issue.key, issue])
  );

  const resolvedMap = new Map(
    resolvedIssues.map(issue => [issue.key, issue])
  );

  const existingTickets = new Map();

  for (const ticket of githubTickets) {
    const key = getSonarKey(ticket);

    if (key) {
      existingTickets.set(key, ticket);
    }
  }

  let created = 0;
  let closed = 0;
  let reopened = 0;
  let skipped = 0;

  // ===================================
  // 1. CREAR TICKETS NUEVOS
  // ===================================

  for (const issue of openIssues) {
    const existing = existingTickets.get(issue.key);

    if (existing) {
      continue;
    }

    await createIssue(issue);
    created++;
  }

  // ===================================
  // 2. ACTUALIZAR TICKETS EXISTENTES
  // ===================================

  for (const [key, ticket] of existingTickets) {

    // Si SonarCloud lo considera abierto
    if (openMap.has(key)) {

      if (ticket.state === "closed") {
        await reopenIssue(ticket);
        reopened++;
      } else {
        console.log(`Sigue abierto: #${ticket.number}`);
        skipped++;
      }

      continue;
    }

    // Si SonarCloud lo considera resuelto
    if (resolvedMap.has(key)) {

      if (ticket.state === "open") {
        await closeIssue(ticket);
        closed++;
      } else {
        console.log(`Ya estaba cerrado: #${ticket.number}`);
        skipped++;
      }

      continue;
    }

    // Si no aparece en ninguna consulta,
    // no asumir que está resuelto.
    console.log(
      `Sin estado confirmado en SonarCloud: #${ticket.number}`
    );

    skipped++;
  }

  // ===================================
  // 3. RESUMEN FINAL
  // ===================================

  console.log("");
  console.log("======================================");
  console.log(" RESUMEN DE SINCRONIZACIÓN");
  console.log("======================================");

  console.log(`Issues abiertos en Sonar: ${openIssues.length}`);
  console.log(`Issues resueltos en Sonar: ${resolvedIssues.length}`);
  console.log(`Tickets creados: ${created}`);
  console.log(`Tickets cerrados: ${closed}`);
  console.log(`Tickets reabiertos: ${reopened}`);
  console.log(`Tickets sin cambios: ${skipped}`);

  console.log("======================================");
  console.log(" SINCRONIZACIÓN FINALIZADA");
  console.log("======================================");
}

main().catch(error => {
  console.error("ERROR EN SINCRONIZACIÓN:", error);
  process.exit(1);
});
