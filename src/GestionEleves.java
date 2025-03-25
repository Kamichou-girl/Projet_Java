import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Eleve {
    private String nom;
    private String prenom;
    private String ville;
    private LocalDate dateNaissance;
    private String classe;

    public Eleve(String nom, String prenom, String ville, LocalDate dateNaissance, String classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.dateNaissance = dateNaissance;
        this.classe = classe;
    }

    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getVille() { return ville; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getClasse() { return classe; }

    // Setters avec validation
    public void setNom(String nom) {
        if (nom != null && !nom.trim().isEmpty()) {
            this.nom = nom;
        }
    }

    public void setPrenom(String prenom) {
        if (prenom != null && !prenom.trim().isEmpty()) {
            this.prenom = prenom;
        }
    }

    public void setVille(String ville) {
        if (ville != null && !ville.trim().isEmpty()) {
            this.ville = ville;
        }
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        if (dateNaissance != null && dateNaissance.isBefore(LocalDate.now())) {
            this.dateNaissance = dateNaissance;
        }
    }

    public void setClasse(String classe) {
        if (classe != null && !classe.trim().isEmpty()) {
            this.classe = classe;
        }
    }

    @Override
    public String toString() {
        return String.format("%-20s %-20s | Ville: %-15s | Naissance: %-10s | Classe: %-5s",
                nom, prenom, ville, dateNaissance, classe);
    }
}

public class GestionEleves {
    private static final List<Eleve> eleves = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        afficherBanniere();
        boolean quitter = false;

        while (!quitter) {
            afficherMenu();
            int choix = saisirChoix();

            switch (choix) {
                case 1 -> ajouterEleve();
                case 2 -> supprimerEleve();
                case 3 -> modifierEleve();
                case 4 -> listerEleves();
                case 5 -> afficherDernierEleve();
                case 0 -> quitter = confirmerQuitter();
                default -> System.out.println("Option invalide !");
            }
        }
        scanner.close();
    }

    private static void afficherBanniere() {
        System.out.println("====================================");
        System.out.println("   APPLICATION DE GESTION DES ÉLÈVES  ");
        System.out.println("====================================");
    }

    private static void afficherMenu() {
        System.out.println("\nMENU PRINCIPAL");
        System.out.println("1. Ajouter un élève");
        System.out.println("2. Supprimer un élève");
        System.out.println("3. Modifier un élève");
        System.out.println("4. Lister tous les élèves");
        System.out.println("5. Afficher le dernier élève ajouté");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private static int saisirChoix() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide : ");
            }
        }
    }

    private static void ajouterEleve() {
        System.out.println("\n--- AJOUT D'UN NOUVEL ÉLÈVE ---");

        String nom = saisirChamp("Nom");
        String prenom = saisirChamp("Prénom");
        String ville = saisirChamp("Ville");
        LocalDate dateNaissance = saisirDateNaissance();
        String classe = saisirChamp("Classe");

        Eleve nouvelEleve = new Eleve(nom, prenom, ville, dateNaissance, classe);
        eleves.add(nouvelEleve);

        System.out.println("\n✅ Élève ajouté avec succès !");
    }

    private static String saisirChamp(String champ) {
        System.out.print(champ + " : ");
        return scanner.nextLine();
    }

    private static LocalDate saisirDateNaissance() {
        while (true) {
            try {
                System.out.print("Date de naissance (AAAA-MM-JJ) : ");
                LocalDate date = LocalDate.parse(scanner.nextLine());

                if (date.isAfter(LocalDate.now())) {
                    System.out.println("La date ne peut pas être dans le futur !");
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Format invalide. Utilisez AAAA-MM-JJ");
            }
        }
    }

    private static void supprimerEleve() {
        if (eleves.isEmpty()) {
            System.out.println("\n⚠️ Aucun élève à supprimer !");
            return;
        }

        listerEleves();
        System.out.print("\nIndex de l'élève à supprimer : ");
        int index = saisirIndex();

        if (index >= 0 && index < eleves.size()) {
            Eleve eleveSupprime = eleves.remove(index);
            System.out.println("\n✅ Élève " + eleveSupprime.getNom() + " supprimé !");
        } else {
            System.out.println("\n❌ Index invalide !");
        }
    }

    private static void modifierEleve() {
        if (eleves.isEmpty()) {
            System.out.println("\n⚠️ Aucun élève à modifier !");
            return;
        }

        listerEleves();
        System.out.print("\nIndex de l'élève à modifier : ");
        int index = saisirIndex();

        if (index >= 0 && index < eleves.size()) {
            Eleve eleve = eleves.get(index);
            System.out.println("\nLaissez vide pour conserver la valeur actuelle");

            System.out.print("Nouveau nom (" + eleve.getNom() + ") : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) eleve.setNom(nom);

            System.out.print("Nouveau prénom (" + eleve.getPrenom() + ") : ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) eleve.setPrenom(prenom);

            System.out.print("Nouvelle ville (" + eleve.getVille() + ") : ");
            String ville = scanner.nextLine();
            if (!ville.isEmpty()) eleve.setVille(ville);

            System.out.print("Nouvelle date de naissance (" + eleve.getDateNaissance() + ") : ");
            String dateStr = scanner.nextLine();
            if (!dateStr.isEmpty()) {
                try {
                    LocalDate nouvelleDate = LocalDate.parse(dateStr);
                    if (nouvelleDate.isBefore(LocalDate.now())) {
                        eleve.setDateNaissance(nouvelleDate);
                    } else {
                        System.out.println("La date ne peut pas être dans le futur !");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Format invalide. La date n'a pas été modifiée.");
                }
            }

            System.out.print("Nouvelle classe (" + eleve.getClasse() + ") : ");
            String classe = scanner.nextLine();
            if (!classe.isEmpty()) eleve.setClasse(classe);

            System.out.println("\n✅ Élève modifié avec succès !");
        } else {
            System.out.println("\n❌ Index invalide !");
        }
    }

    private static int saisirIndex() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide : ");
            }
        }
    }

    private static void listerEleves() {
        if (eleves.isEmpty()) {
            System.out.println("\n⚠️ Aucun élève enregistré !");
            return;
        }

        System.out.println("\n--- LISTE DES ÉLÈVES (" + eleves.size() + ") ---");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("%-5s %-20s %-20s %-15s %-12s %-5s%n",
                "ID", "Nom", "Prénom", "Ville", "Naissance", "Classe");
        System.out.println("-------------------------------------------------------------------------------");

        for (int i = 0; i < eleves.size(); i++) {
            Eleve e = eleves.get(i);
            System.out.printf("%-5d %-20s %-20s %-15s %-12s %-5s%n",
                    i, e.getNom(), e.getPrenom(), e.getVille(),
                    e.getDateNaissance(), e.getClasse());
        }
    }

    private static void afficherDernierEleve() {
        if (eleves.isEmpty()) {
            System.out.println("\n⚠️ Aucun élève enregistré !");
            return;
        }

        Eleve dernier = eleves.get(eleves.size() - 1);
        System.out.println("\n--- DERNIER ÉLÈVE AJOUTÉ ---");
        System.out.println("Nom: " + dernier.getNom());
        System.out.println("Prénom: " + dernier.getPrenom());
        System.out.println("Ville: " + dernier.getVille());
        System.out.println("Date de naissance: " + dernier.getDateNaissance());
        System.out.println("Classe: " + dernier.getClasse());
    }

    private static boolean confirmerQuitter() {
        System.out.print("\nVoulez-vous vraiment quitter ? (O/N) : ");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("O")) {
            System.out.println("\nMerci d'avoir utilisé l'application. Au revoir !");
            return true;
        }
        return false;
    }
}