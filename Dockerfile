# Image officielle avec Java 17 complet
FROM eclipse-temurin:17-jdk

# Installation des bibliothèques X11 et polices requises par Java AWT/Swing
RUN apt-get update && apt-get install -y --no-install-recommends \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

# Dossier de travail à l'intérieur du conteneur
WORKDIR /app

# Copie des dossiers sources et des ressources (icônes)
COPY metier ./metier
COPY controleur ./controleur
COPY ihm ./ihm
COPY images ./images

# Compilation des fichiers Java en UTF-8
RUN mkdir -p bin && javac -encoding UTF-8 -d bin metier/*.java controleur/*.java ihm/*.java

# Lancement du jeu
CMD ["java", "-cp", "bin", "controleur.Controleur"]