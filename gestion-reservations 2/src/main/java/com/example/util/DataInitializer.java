package com.example.util;

import com.example.model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Random;

public class DataInitializer {

    private final EntityManagerFactory emf;
    private final Random random = new Random();

    public DataInitializer(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void initializeData() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            System.out.println("Creation des equipements...");
            Equipement[] equipements = createEquipements(em);

            System.out.println("Creation des utilisateurs...");
            Utilisateur[] utilisateurs = createUtilisateurs(em);

            System.out.println("Creation des salles...");
            Salle[] salles = createSalles(em, equipements);

            System.out.println("Creation des reservations...");
            createReservations(em, utilisateurs, salles);

            em.getTransaction().commit();
            System.out.println("Jeu de donnees initialise avec succes !");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Erreur lors de l initialisation des donnees");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private Equipement[] createEquipements(EntityManager em) {
        Equipement[] equipements = new Equipement[10];

        equipements[0] = new Equipement("Projecteur HD", "Projecteur haute definition 4K");
        equipements[0].setReference("PROJ-4K-001");

        equipements[1] = new Equipement("Ecran interactif", "Ecran tactile 65 pouces");
        equipements[1].setReference("ECRAN-T-65");

        equipements[2] = new Equipement("Systeme de visioconference", "Systeme complet avec camera HD");
        equipements[2].setReference("VISIO-HD-100");

        equipements[3] = new Equipement("Tableau blanc", "Tableau blanc magnetique 2m x 1m");
        equipements[3].setReference("TB-MAG-2X1");

        equipements[4] = new Equipement("Systeme audio", "Systeme audio avec 4 haut-parleurs");
        equipements[4].setReference("AUDIO-4HP");

        equipements[5] = new Equipement("Microphones sans fil", "Set de 4 microphones sans fil");
        equipements[5].setReference("MIC-SF-4");

        equipements[6] = new Equipement("Ordinateur fixe", "PC avec Windows 11 et suite Office");
        equipements[6].setReference("PC-W11-OFF");

        equipements[7] = new Equipement("Connexion WiFi haut debit", "WiFi 6 jusqu a 1 Gbps");
        equipements[7].setReference("WIFI-6-1G");

        equipements[8] = new Equipement("Systeme de climatisation", "Climatisation reglable");
        equipements[8].setReference("CLIM-REG");

        equipements[9] = new Equipement("Prises electriques multiples", "10 prises electriques reparties");
        equipements[9].setReference("PRISES-10");

        for (Equipement e : equipements) {
            em.persist(e);
        }

        return equipements;
    }

    private Utilisateur[] createUtilisateurs(EntityManager em) {
        Utilisateur[] utilisateurs = new Utilisateur[20];

        String[] noms = {
                "Martin", "Bernard", "Dubois", "Thomas", "Robert",
                "Richard", "Petit", "Durand", "Leroy", "Moreau",
                "Simon", "Laurent", "Lefebvre", "Michel", "Garcia",
                "David", "Bertrand", "Roux", "Vincent", "Fournier"
        };

        String[] prenoms = {
                "Jean", "Marie", "Pierre", "Sophie", "Thomas",
                "Catherine", "Nicolas", "Isabelle", "Philippe", "Nathalie",
                "Michel", "Francoise", "Patrick", "Monique", "Rene",
                "Sylvie", "Louis", "Anne", "Daniel", "Christine"
        };

        String[] departements = {
                "Ressources Humaines", "Informatique", "Finance", "Marketing", "Commercial",
                "Production", "Recherche et Developpement", "Juridique", "Communication", "Direction"
        };

        for (int i = 0; i < utilisateurs.length; i++) {
            String nom = noms[i];
            String prenom = prenoms[i];
            String email = prenom.toLowerCase() + "." + nom.toLowerCase() + "@example.com";

            Utilisateur u = new Utilisateur(nom, prenom, email);

            u.setTelephone("06" + (10000000 + random.nextInt(90000000)));
            u.setDepartement(departements[i % departements.length]);

            em.persist(u);
            utilisateurs[i] = u;
        }

        return utilisateurs;
    }

    private Salle[] createSalles(EntityManager em, Equipement[] equipements) {
        Salle[] salles = new Salle[15];

        for (int i = 0; i < 5; i++) {
            Salle s = new Salle("Salle A" + (i + 1), 10 + i * 2);
            s.setDescription("Salle de reunion standard");
            s.setBatiment("Batiment A");
            s.setEtage((i % 3) + 1);
            s.setNumero("A" + (i + 1));

            s.addEquipement(equipements[3]);
            s.addEquipement(equipements[7]);
            s.addEquipement(equipements[9]);

            if (i % 2 == 0) s.addEquipement(equipements[0]);
            if (i % 3 == 0) s.addEquipement(equipements[4]);

            em.persist(s);
            salles[i] = s;
        }

        for (int i = 5; i < 10; i++) {
            Salle s = new Salle("Salle B" + (i - 4), 20 + (i - 5) * 5);
            s.setDescription("Salle de formation equipee");
            s.setBatiment("Batiment B");
            s.setEtage((i % 4) + 1);
            s.setNumero("B" + (i - 4));

            s.addEquipement(equipements[0]);
            s.addEquipement(equipements[3]);
            s.addEquipement(equipements[6]);
            s.addEquipement(equipements[7]);
            s.addEquipement(equipements[9]);

            if (i % 2 == 0) s.addEquipement(equipements[1]);

            em.persist(s);
            salles[i] = s;
        }

        for (int i = 10; i < 15; i++) {
            Salle s = new Salle("Salle C" + (i - 9), 50 + (i - 10) * 20);
            s.setDescription("Salle de conference haut de gamme");
            s.setBatiment("Batiment C");
            s.setEtage((i % 3) + 1);
            s.setNumero("C" + (i - 9));

            s.addEquipement(equipements[0]);
            s.addEquipement(equipements[2]);
            s.addEquipement(equipements[4]);
            s.addEquipement(equipements[5]);
            s.addEquipement(equipements[7]);
            s.addEquipement(equipements[8]);
            s.addEquipement(equipements[9]);

            em.persist(s);
            salles[i] = s;
        }

        return salles;
    }

    private void createReservations(EntityManager em, Utilisateur[] utilisateurs, Salle[] salles) {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        String[] motifs = {
                "Reunion d equipe", "Entretien", "Formation", "Presentation client",
                "Brainstorming", "Reunion de projet", "Conference", "Atelier",
                "Seminaire", "Reunion de direction", "Demonstration produit"
        };

        int created = 0;
        int target = 100;
        int maxAttempts = 2000;
        int attempts = 0;

        while (created < target && attempts < maxAttempts) {
            attempts++;

            int jourOffset = random.nextInt(90);
            int heureDebut = 8 + random.nextInt(9);
            int duree = 1 + random.nextInt(3);

            LocalDateTime dateDebut = now.plusDays(jourOffset).withHour(heureDebut);
            LocalDateTime dateFin = dateDebut.plusHours(duree);

            Utilisateur utilisateur = utilisateurs[random.nextInt(utilisateurs.length)];
            Salle salle = salles[random.nextInt(salles.length)];

            if (hasConflict(em, salle.getId(), dateDebut, dateFin)) {
                continue;
            }

            Reservation r = new Reservation(dateDebut, dateFin, motifs[random.nextInt(motifs.length)]);

            int statutRandom = random.nextInt(10);
            if (statutRandom < 8) r.setStatut(StatutReservation.CONFIRMEE);
            else if (statutRandom < 9) r.setStatut(StatutReservation.EN_ATTENTE);
            else r.setStatut(StatutReservation.ANNULEE);

            utilisateur.addReservation(r);
            salle.addReservation(r);

            em.persist(r);
            created++;
        }

        System.out.println("Reservations creees: " + created + " (tentatives: " + attempts + ")");

        if (created < target) {
            System.out.println("Pas pu atteindre " + target + " reservations a cause des conflits.");
        }
    }

    private boolean hasConflict(EntityManager em, Long salleId, LocalDateTime debut, LocalDateTime fin) {
        TypedQuery<Long> q = em.createQuery(
                "select count(r) " +
                        "from Reservation r " +
                        "where r.salle.id = :salleId " +
                        "and r.statut <> :annulee " +
                        "and r.dateDebut < :fin " +
                        "and r.dateFin > :debut",
                Long.class
        );

        Long count = q.setParameter("salleId", salleId)
                .setParameter("annulee", StatutReservation.ANNULEE)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getSingleResult();

        return count != null && count > 0;
    }

    @SuppressWarnings("unused")
    private void clearAll(EntityManager em) {
        em.createQuery("delete from Reservation").executeUpdate();
        em.createQuery("delete from Salle").executeUpdate();
        em.createQuery("delete from Utilisateur").executeUpdate();
        em.createQuery("delete from Equipement").executeUpdate();
    }
}