-- Nettoyage des tables (ordre important pour respecter les contraintes de clés étrangères)
DELETE FROM reservation;
DELETE FROM restaurant_table;
DELETE FROM utilisateur;
DELETE FROM role;

-- Insertion des rôles
INSERT INTO role (id, name) VALUES
(1, 'CLIENT'),
(2, 'SERVEUR'),
(3, 'ADMIN');

-- Insertion des utilisateurs de test
INSERT INTO utilisateur (id, username, email, password, role_id) VALUES
(1, 'client1', 'client1@example.com', 'password', 1),
(2, 'client2', 'client2@example.com', 'password', 1);

-- Insertion des tables
INSERT INTO restaurant_table (id, numero, capacite) VALUES
(1, 1, 2),
(2, 2, 4),
(3, 3, 6);

-- Insertion des réservations pour la date du test
INSERT INTO reservation (id_reservation, date_reservation, creneau_horaire, nb_personne, client_id, table_id) VALUES
(1, '2025-05-05', 'midi', 2, 1, 1),
(2, '2025-05-05', 'soir', 3, 2, 2),
(3, '2025-05-06', 'midi', 5, 1, 3);