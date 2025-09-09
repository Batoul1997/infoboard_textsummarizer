FROM nginx:alpine

# ---- Kopiert alle Dateien aus deinem Projektordner ins Standard-Webroot von NGINX ----
COPY . /usr/share/nginx/html


 #--- Überschreibt die Standard-Config mit deiner eigenen nginx.conf ---
COPY nginx.conf /etc/nginx/conf.d/default.conf