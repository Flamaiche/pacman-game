#!/bin/bash

# Parcourt récursivement tous les fichiers .png à partir du dossier courant
find . -type f -name "*.png" | while read -r file; do
    # Extraire le dossier et le nom de fichier
    dir=$(dirname "$file")
    name=$(basename "$file")

    case "$name" in
        3.png|5.png|7.png)
            new_name="1.png"
            ;;
        4.png|6.png|8.png)
            new_name="2.png"
            ;;
        *)
            continue
            ;;
    esac

    new_path="$dir/$new_name"

    # Vérifie si le fichier cible existe déjà
    if [[ -e "$new_path" ]]; then
        echo "⚠️  Le fichier $new_path existe déjà, renommage ignoré."
    else
        echo "Renommage : $file → $new_path"
        mv "$file" "$new_path"
    fi
done
