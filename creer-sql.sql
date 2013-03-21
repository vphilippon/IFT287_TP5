/*
Creer BD
	Guillaume Harvey
	Kevin Labrie
	Mathieu Larocque
	Vincent Philippon
*/


/* PERSONNE */
DROP TABLE IF EXISTS Personne CASCADE;
CREATE TABLE Personne (
	nom VARCHAR(255) NOT NULL,
	dateNaissance DATE NOT NULL,
	lieuNaissance VARCHAR(255),
	sexe INTEGER NOT NULL CHECK(sexe = 0 OR sexe = 1),
	CONSTRAINT personnePK PRIMARY KEY (nom)
);

 /* FILM */ 
DROP TABLE IF EXISTS Film CASCADE;
CREATE TABLE Film (
	titre VARCHAR(255) NOT NULL,
	dateSortie DATE NOT NULL,
	description VARCHAR(255),
	duree INTEGER,
	realisateur VARCHAR(255) NOT NULL,
	CONSTRAINT filmPK PRIMARY KEY (titre, dateSortie),
	CONSTRAINT filmFK FOREIGN KEY (realisateur) REFERENCES Personne(nom)
 );
 
/* ROLE_FILM */
DROP TABLE IF EXISTS RoleFilm CASCADE;
CREATE TABLE RoleFilm (
	nomActeur VARCHAR(255) NOT NULL,
	roleActeur VARCHAR(255) NOT NULL,
	filmTitre VARCHAR(255) NOT NULL,
	anneeSortie DATE NOT NULL,
	CONSTRAINT roleFilmPK PRIMARY KEY (nomActeur, filmTitre, anneeSortie),
	CONSTRAINT roleFilmTitreFK FOREIGN KEY (filmTitre, anneeSortie) REFERENCES Film(titre, dateSortie),
	CONSTRAINT roleFilmActeurFK FOREIGN KEY (nomActeur) REFERENCES Personne(nom)
);
  
/* SERIE */
DROP TABLE IF EXISTS Serie CASCADE;
CREATE TABLE Serie (
	titre VARCHAR(255) NOT NULL,
	anneeSortie DATE NOT NULL,
	realisateur VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	nbSaison INTEGER NOT NULL,
	CONSTRAINT seriePK PRIMARY KEY (titre, anneeSortie),
	CONSTRAINT serieFK FOREIGN KEY (realisateur) REFERENCES Personne(nom)
);
	
/* EPISODE */
DROP TABLE IF EXISTS Episode CASCADE;
CREATE TABLE Episode (
	titre VARCHAR(255) NOT NULL,
	titreSerie VARCHAR(255) NOT NULL,
	anneeSortieSerie DATE NOT NULL,
	noSaison INTEGER NOT NULL,
	noEpisode INTEGER NOT NULL, 
	description VARCHAR(255),
	dateDiffusion DATE NOT NULL,
	CONSTRAINT episodePK PRIMARY KEY (titreSerie, anneeSortieSerie, noSaison, noEpisode),
	CONSTRAINT episodeFK FOREIGN KEY (titreSerie, anneeSortieSerie) REFERENCES Serie(titre, anneeSortie),
	CONSTRAINT noSaisonCHK CHECK(noSaison > 0),
	CONSTRAINT noEpisodeCHK CHECK (noEpisode > 0)
);

/* ROLE_EPISODE */
DROP TABLE IF EXISTS RoleEpisode CASCADE;
CREATE TABLE RoleEpisode(
	nomActeur VARCHAR(255) NOT NULL,
	roleActeur VARCHAR(255) NOT NULL,
	titreSerie VARCHAR(255) NOT NULL,
	noSaison INTEGER NOT NULL,
	noEpisode INTEGER NOT NULL,
	anneeSortieSerie DATE NOT NULL,
	CONSTRAINT roleEpisodePK PRIMARY KEY (nomActeur, titreSerie, anneeSortieSerie, noSaison, noEpisode),	
	CONSTRAINT roleEpisodeFK1 FOREIGN KEY (nomActeur) REFERENCES Personne(nom),	
	CONSTRAINT roleEpisodeFK2 FOREIGN KEY (titreSerie, noSaison, noEpisode, anneeSortieSerie) REFERENCES Episode(titreSerie, noSaison, noEpisode, anneeSortieSerie)
);

