# PROJET_ANDROID
application de partage de photo à l'aide du framework firebase


L'application dispose de nombreuses fonctionnalités:

un système d'authentification et de creation de compte (Firebase Authentification)

un système de notification qui notifie quel utilisateur a posté une nouvelle photo. Clickée, elle amène l'utilisateur directement sur la page du fil de photos (Firebase FCM + okhttp)

Une gestion de la geolocalisation user friendly

Un partage de photos rapide avec leur différentes données (nom de fichier, taille de fichier, position et date) (Firebase Realtime database)

Un affichage synthétique et rapide des photos à la facon (Instagram) (Firebase Realtime database + Glide )

Une navigation intuitive et belle (Android Navigation Drawer)

L'application possède cependant certaines faiblesses

Lorsque l'utilisateur n'a pas la géolocalisation activée, l'application propose de l'activer. Apres la prise de la photo le champ de géolocalisation est vide toutefois la donnée est bien envoyée au serveur

Certaines photos ne peuvent ne pas s'afficher, le problème venant de glide qui refuse certains liens de photos
