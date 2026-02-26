# Deployment on Azure VM (No Docker)

This runbook describes the production demo setup used for this project.

## Target runtime

- VM: Azure Linux VM
- App runtime: Java 17, Spring Boot jar (`app.jar`)
- Process manager: `systemd` (`store-api.service`)
- Reverse proxy: Nginx (`80 -> 8080`)
- Database: local MySQL service

## 1) Prerequisites on VM

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk mysql-server nginx git
java -version
```

## 2) Database setup

```bash
sudo mysql <<'SQL'
CREATE DATABASE IF NOT EXISTS store_api;
CREATE USER IF NOT EXISTS 'store_user'@'localhost' IDENTIFIED BY 'CHANGE_ME_DB_PASSWORD';
GRANT ALL PRIVILEGES ON store_api.* TO 'store_user'@'localhost';
FLUSH PRIVILEGES;
SQL
```

## 3) Deploy jar

Build locally and upload:

```bash
./mvnw -DskipTests clean package
scp target/app.jar <vm-user>@<vm-host>:~/store-api/app.jar
```

## 4) App environment file on VM

Create `~/store-api/.env` with plain `KEY=value` lines (no `export`):

```dotenv
SPRING_PROFILES_ACTIVE=prod
DB_NAME=store_api
DB_USER=store_user
DB_PASSWORD=CHANGE_ME_DB_PASSWORD
JWT_SECRET=CHANGE_ME_STRONG_SECRET
STRIPE_SECRET_KEY=change_me_stripe_secret_key
STRIPE_WEBHOOK_SECRET_KEY=change_me_stripe_webhook_secret
JAVA_OPTS=-Xms128m -Xmx320m
```

If uploaded from Windows, normalize line endings:

```bash
sed -i 's/\r$//' ~/store-api/.env
```

## 5) systemd service

```bash
sudo tee /etc/systemd/system/store-api.service >/dev/null <<'EOF'
[Unit]
Description=Store API
After=network.target mysql.service
Wants=mysql.service

[Service]
User=ghassene
WorkingDirectory=/home/ghassene/store-api
ExecStart=/usr/bin/java -Xms128m -Xmx320m -jar /home/ghassene/store-api/app.jar --server.port=8080
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable store-api
sudo systemctl restart store-api
sudo systemctl status store-api --no-pager
```

## 6) Nginx reverse proxy

```bash
sudo tee /etc/nginx/sites-available/store-api >/dev/null <<'EOF'
server {
  listen 80;
  server_name _;
  location / {
    proxy_pass http://127.0.0.1:8080;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
EOF

sudo rm -f /etc/nginx/sites-enabled/default
sudo ln -sf /etc/nginx/sites-available/store-api /etc/nginx/sites-enabled/store-api
sudo nginx -t
sudo systemctl restart nginx
```

## 7) Validation

```bash
curl -I http://localhost:8080/products
curl -I http://localhost/swagger-ui.html
```

Public checks:

- `http://<vm-host>/products`
- `http://<vm-host>/swagger-ui.html`

## 8) Common issues

- `Malformed entry export ...` -> `.env` contains `export`; use `KEY=value` format.
- `Access denied ... (using password: NO)` -> DB user/password not correctly read; verify `.env` and MySQL user grants.
- VM memory pressure -> increase VM RAM or reduce Java heap (`-Xmx320m`).
