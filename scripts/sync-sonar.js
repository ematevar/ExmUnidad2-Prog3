
const SONAR_URL = process.env.SONAR_HOST_URL;
const SONAR_TOKEN = process.env.SONAR_TOKEN;
const PROJECT_KEY = process.env.SONAR_PROJECT_KEY;

const [owner, repo] = process.env.GITHUB_REPOSITORY.split("/");
const GITHUB_TOKEN = process.env.GITHUB_TOKEN;

const API = "https://api.github.com";
const LABEL = "sonarqube";

async function github(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: {
      Accept: "application/vnd.github+json",
      "X-GitHub-Api-Version": "2022-11-28",
      Authorization: `Bearer ${GITHUB_TOKEN}`,
      ...options.headers
    }
  });

  if (!response.ok) {
    throw new Error(
      `GitHub error ${response.status}: ${await response.text()}`
    );
  }

  return response.json();
}

async function getSonarIssues() {
  const auth = Buffer.from(`${SONAR_TOKEN}:`).toString("base64");
  const issues = [];

  for (let page = 1; ; page++) {
    const params = new URLSearchParams({
      componentKeys: PROJECT_KEY,
      resolved: "false",
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
      throw new Error(`SonarCloud error: ${response.status}`);
    }

    const data = await response.json();

    issues.push(...data.issues);

    if (issues.length >= data.total || data.issues.length === 0) {
      break;
    }
  }

  return issues;
}

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

    tickets.push(...data.filter(i => !i.pull_request));

    if (data.length < 100) break;
  }

  return tickets;
}

async function createIssue(issue) {
  const file = issue.component.replace(`${PROJECT_KEY}:`, "");

  const sonarLink =
    `${SONAR_URL}/project/issues?id=${encodeURIComponent(PROJECT_KEY)}` +
    `&open=${encodeURIComponent(issue.key)}`;

  const title = `[SonarQube] [${issue.severity}] ${issue.message}`;

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

  await github(`${API}/repos/${owner}/${repo}/issues`, {
    method: "POST",
    body: JSON.stringify({
      title,
      body,
      labels: [LABEL]
    })
  });

  console.log(`Ticket creado: ${issue.key}`);
}

async function main() {
  console.log("Iniciando sincronización...");

  const sonarIssues = await getSonarIssues();
  const githubTickets = await getExistingTickets();

  const existingKeys = new Set();

  for (const ticket of githubTickets) {
    const match = ticket.body?.match(
      /<!-- sonarcloud-issue-key: ([^ ]+) -->/
    );

    if (match) existingKeys.add(match[1]);
  }

  let created = 0;

  for (const issue of sonarIssues) {
    if (existingKeys.has(issue.key)) {
      console.log(`Ya existe: ${issue.key}`);
      continue;
    }

    await createIssue(issue);
    existingKeys.add(issue.key);
    created++;
  }

  console.log("--------------------------------");
  console.log(`Issues encontrados: ${sonarIssues.length}`);
  console.log(`Tickets creados: ${created}`);
  console.log("--------------------------------");
}

main().catch(error => {
  console.error(error);
  process.exit(1);
});
