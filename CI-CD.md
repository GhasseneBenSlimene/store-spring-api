# 🚀 CI/CD Document

## GitHub Actions Pipeline

### Badges de Statut

Ajoute ces badges à ton README.md:

```markdown
![CI/CD Pipeline](https://github.com/GhasseneBenSlimene/store-api/actions/workflows/ci-cd.yml/badge.svg)
![Tests](https://img.shields.io/badge/tests-17%20passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-33%25-yellow)
![Java](https://img.shields.io/badge/java-17-orange)
![Spring Boot](https://img.shields.io/badge/springboot-3.4.1-green)
```

### Workflow Déclenché par:

- ✅ Push sur `main` ou `develop`
- ✅ Pull Request vers `main` ou `develop`

---

## Jobs du Pipeline

### 1️⃣ Build & Test

**Durée:** ~2 minutes
**Actions:**

- Checkout du code
- Setup Java 17
- Build avec Maven
- Exécution des 17 tests
- Génération du rapport JaCoCo
- Upload vers Codecov

```yaml
Run: ./mvnw clean test
```

### 2️⃣ Lint & Code Quality

**Durée:** ~1 minute
**Actions:**

- SpotBugs: Détecte les bugs potentiels
- Checkstyle: Vérifie le style du code
- PMD: Analyse la qualité du code

```bash
# Lancer manuellement
./mvnw spotbugs:check
./mvnw checkstyle:check
./mvnw pmd:check
```

### 3️⃣ Security Scan

**Durée:** ~2 minutes
**Actions:**

- Dependency-Check: Scanne les CVE
- Vérifie les packages malveillants

### 4️⃣ Docker Build & Push

**Déclenché:** Seulement sur push vers `main`
**Actions:**

- Build l'image Docker
- Tag avec `latest` et le commit SHA
- Push vers Docker Hub

---

## Configuration GitHub Secrets

Ajoute ces secrets à ton repo GitHub:

### 1. Accéder aux Secrets

```
Settings → Secrets and variables → Actions
```

### 2. Secrets Requis

#### Docker Hub

```
DOCKER_USERNAME = [ton_username_dockerhub]
DOCKER_PASSWORD = [ton_token_dockerhub]
```

**Comment générer le token:**

1. Docker Hub → Account Settings → Security
2. New Access Token
3. Copier et ajouter comme secret

#### Codecov (Optional)

```
CODECOV_TOKEN = [ton_token_codecov]
```

**Comment obtenir:**

1. https://codecov.io/
2. Connecter avec GitHub
3. Copy token

#### SonarQube (Optional)

```
SONAR_TOKEN = [token_sonar]
SONAR_HOST_URL = [host_url]
```

---

## Résultats du Pipeline

### ✅ Succès

- Tous les tests passent
- Code build sans erreurs
- Image Docker créée et pushée
- Badge affiche "passing" ✅

### ❌ Échec

- Tests échouent → Build arrêté
- Linting errors → Pipeline warning
- Push Docker échoue → Vérifier les secrets

---

## Voir les Résultats

**Dans GitHub:**

```
Repo → Actions → Cliquer sur le dernier run
```

**Détails:**

```
- Build logs
- Test results
- Coverage reports
- Docker push status
```

---

## Exécuter Localement (Sans GitHub)

```bash
# Build & Test
./mvnw clean test

# Lint
./mvnw spotbugs:check checkstyle:check pmd:check

# Coverage
./mvnw test jacoco:report
# Ouvrir: target/site/jacoco/index.html

# Docker
docker build -t store-api:latest .
docker-compose up -d
```

---

## Intégrations (À Configurer)

### Codecov (Coverage Tracking)

```bash
# Activer:
1. https://codecov.io/gh
2. Connecter le repo GitHub
3. Ajouter CODECOV_TOKEN aux secrets
```

### SonarQube (Code Quality)

```bash
# Activer:
1. Installer SonarQube localement ou Cloud
2. Ajouter secrets SONAR_TOKEN et SONAR_HOST_URL
```

### Slack Notifications

```yaml
# Ajouter dans le workflow:
- uses: slack-notify@v1
  if: failure()
  with:
    channel: "#ci-cd"
    message: "Pipeline failed"
```

---

## Optimisations

### Paralléliser les Jobs

```yaml
# Actuellement:
build-and-test → lint → security

# Peut être parallélisé:
build-and-test
lint
security
```

### Cache des Dépendances

```yaml
- uses: actions/setup-java@v4
  with:
    cache: maven # ← Cache automatique
```

Réduit le temps de setup de ~30-40%.

---

## Troubleshooting

### ❌ Tests échouent sur GitHub mais passent localement

```bash
# Vérifier:
1. Java version: java -version
2. Maven: ./mvnw -v
3. Dépendances: ./mvnw dependency:resolve
4. Tests isolés: ./mvnw test -Dtest=NomDuTest
```

### ❌ Docker push échoue

```bash
# Vérifier les secrets:
1. DOCKER_USERNAME correct
2. DOCKER_PASSWORD est un token (pas le mot de passe)
3. Compte Docker public ou token a les permissions

# Tester localement:
docker login
docker push ghassenebenslimene/store-api:test
```

### ❌ Coverage report ne se génère pas

```bash
# Assurer que JaCoCo est configuré:
./mvnw test jacoco:report

# Check: target/site/jacoco/index.html existe
```

---

## Exemple de Résultat

```
✅ CI/CD Pipeline
├── ✅ build-and-test (2m 15s)
│   ├── ✅ Checkout Code
│   ├── ✅ Setup Java 17
│   ├── ✅ Build with Maven
│   ├── ✅ Run Tests (17 passed)
│   └── ✅ Coverage Report
├── ✅ lint (1m 30s)
│   ├── ✅ SpotBugs
│   ├── ✅ Checkstyle
│   └── ✅ PMD
├── ✅ security-scan (2m 45s)
│   └── ✅ Dependency-Check
└── ✅ Docker Build & Push (3m)
    └── ✅ Image pushed: store-api:latest
```

---

## Documents Connexes

- [DOCKER.md](DOCKER.md) - Docker setup
- [Makefile](Makefile) - Local commands
- [.github/workflows/ci-cd.yml](.github/workflows/ci-cd.yml) - Workflow source
