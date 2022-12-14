create database db_gestionale;
use db_gestionale;

create table Categoria (
     nome varchar(40) not null,
     constraint IDCategoria primary key (nome));

create table Cliente_Tesserato (
     data_tesseramento date not null,
     numero_tessera int not null auto_increment,
     email varchar(40) not null,
     constraint IDCliente_Tesserato primary key (numero_tessera),
     constraint FKcliente_mail_ID unique (email));

create table Composizione (
     codice_prod int not null,
     quantità int not null check(quantità > 0),
     codice_dipendente int not null,
     giorno_saldo date not null,
     codice_fornitore int not null,
     constraint IDComposizione primary key (codice_prod, codice_dipendente, giorno_saldo, codice_fornitore));

create table Costi_di_gestione (
     id_costo int not null auto_increment,
     causale varchar(50) not null,
     breve_descrizione varchar(200) not null,
     importo DECIMAL(6, 2) not null check (importo > 0),
     giorno_saldo date not null,
     codice_dipendente int not null,
     constraint IDCosti_di_gestione primary key (id_costo));

create table Dipendente (
     data_assunzione date not null,
     stipendio_base DECIMAL(6, 2) not null,
     ore_lavoro_mensili float(1) not null,
     codice_dipendente int(10) not null auto_increment,
     email varchar(40) not null,
     è_responsabile bool not null,
     licenziato bool not null default false,
     password varchar(45),
     constraint IDDipendente primary key (codice_dipendente),
     constraint FKdipendente_mail_ID unique (email));

create table Fondo_Cassa (
     numero_cassa int not null auto_increment,
     importo DECIMAL(6,2) not null,
     data_aggiornamento datetime not null,
     codice_dipendente int not null,
     constraint IDFondo_Cassa primary key (numero_cassa, data_aggiornamento));

create table Fornitore (
     codice_fornitore int(10) not null auto_increment,
     nome varchar(30) not null,
     constraint IDFornitore primary key (codice_fornitore));

create table Ordine (
     codice_dipendente int not null,
     giorno_saldo date not null,
     codice_fornitore int not null,
     constraint IDOrdine_ID primary key (codice_dipendente, giorno_saldo, codice_fornitore));

create table Persona (
     nome varchar(15) not null,
     cognome varchar(15) not null,
     email varchar(40) not null,
     città varchar(40) not null,
     constraint IDPersona_ID primary key (email));

create table Prodotto (
     codice_prod int(10) not null,
     nome_prodotto varchar(40) not null,
     prezzo_vendita DECIMAL(6, 2) not null,
     prezzo_acquisto DECIMAL(6, 2) not null,
     quantità_disponibile int not null,
     categoria varchar(40) not null,
     constraint IDProdotto_ID primary key (codice_prod));

create table prodotto_di_fornitore (
     codice_prod int(10) not null,
     codice_fornitore int(10) not null,
     constraint FKè_un_ID primary key (codice_prod));

create table Prodotto_in_Vendita (
     codice_prod int(10) not null,
     quantità int not null,
     codice_scontrino int not null,
     prezzo_quando_venduto DECIMAL(6,2) not null,
     constraint IDProdotto_in_Vendita primary key (codice_prod, codice_scontrino));

create table Ricerca (
     id_ricerca int not null auto_increment,
     data date not null,
     categoria varchar(40),
     nome varchar(100),
     constraint IDRicerca primary key (id_ricerca));

create table Saldo_giornaliero (
     data date not null,
     entrate DECIMAL(6, 2) not null,
     uscite DECIMAL(6, 2) not null,
     constraint IDSaldo_giornaliero primary key (data));

create table Stipendio (
     codice_beneficiario int not null auto_increment,
     importo DECIMAL(6, 2) not null,
     giorno_saldo date not null,
     codice_assegnatore int not null,
     constraint IDStipendio primary key (codice_beneficiario, giorno_saldo));

create table Turno (
     codice_dipendente int not null auto_increment,
     data datetime not null,
     durata time check (durata >= 0),
     constraint IDTurno primary key (codice_dipendente, data));

create table Vendita (
     codice_scontrino int not null auto_increment,
     giorno_saldo date not null,
     codice_dipendente int(10) not null,
     numero_cliente_tesserato int,
     constraint IDVendita_ID primary key (codice_scontrino),
     constraint IDVendita_2 unique (codice_scontrino, giorno_saldo));

create table Video (
     id_videocamera int not null,
     data_inizio datetime not null,
     data_fine datetime not null,
     video_path varchar(100) not null,
     valido boolean default true not null,
     constraint IDVideo primary key (id_videocamera, data_inizio));




alter table Cliente_Tesserato add constraint FKcliente_mail_FK
     foreign key (email)
     references Persona (email)
     on update cascade
     on delete cascade;

alter table Composizione add constraint FKprodotto
     foreign key (codice_prod)
     references Prodotto (codice_prod);

alter table Composizione add constraint FKcomposto
     foreign key (codice_dipendente, giorno_saldo, codice_fornitore)
     references Ordine (codice_dipendente, giorno_saldo, codice_fornitore);

alter table Costi_di_gestione add constraint FKgestione_compone 
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Costi_di_gestione add constraint FKgestione_inserisce
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Dipendente add constraint FKdipendente_mail_FK
     foreign key (email)
     references Persona (email);

alter table Fondo_Cassa add constraint FKinit
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Ordine add constraint FKordine_compone 
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Ordine add constraint FKordine_sottomette
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Ordine add constraint FKriceve
     foreign key (codice_fornitore)
     references Fornitore (codice_fornitore);

alter table Prodotto add constraint FKappartiene
     foreign key (categoria)
     references Categoria (nome);

alter table Prodotto add constraint is_positive
	CHECK (quantità_disponibile >= 0);

alter table prodotto_di_fornitore add constraint FKdispone
     foreign key (codice_fornitore)
     references Fornitore (codice_fornitore);

alter table prodotto_di_fornitore add constraint FKè_un_FK
     foreign key (codice_prod)
     references Prodotto (codice_prod);

alter table Prodotto_in_Vendita add constraint FKprod_in_vendita_è
     foreign key (codice_prod)
     references Prodotto (codice_prod);

alter table Prodotto_in_Vendita add constraint FKcontiene
     foreign key (codice_scontrino)
     references Vendita (codice_scontrino);

alter table Stipendio add constraint FKstipendio_compone
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Stipendio add constraint FKstipendio_assegna
     foreign key (codice_assegnatore)
     references Dipendente (codice_dipendente);

alter table Stipendio add constraint FKstipendio_percepisce
     foreign key (codice_beneficiario)
     references Dipendente (codice_dipendente);

alter table Turno add constraint FKsvolge
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Vendita add constraint FKvendita_compone 
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Vendita add constraint FKvendita_effettua
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Vendita add constraint FKvendita_partecipa
     foreign key (numero_cliente_tesserato)
     references Cliente_Tesserato (numero_tessera);

DELIMITER $$
CREATE TRIGGER dopo_nuovo_prodotto_in_vendita
AFTER INSERT
ON prodotto_in_vendita FOR EACH ROW
BEGIN
	UPDATE prodotto set quantità_disponibile = quantità_disponibile - NEW.quantità where prodotto.codice_prod = NEW.codice_prod;
END$$
DELIMITER ;


DELIMITER $$
CREATE TRIGGER nuova_vendita
BEFORE INSERT
ON vendita FOR EACH ROW
BEGIN
	IF NEW.giorno_saldo NOT IN (SELECT data FROM saldo_giornaliero) THEN
		INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES (new.giorno_saldo, 0, 0);
        END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER nuovo_ordine
BEFORE INSERT
ON ordine FOR EACH ROW
BEGIN
	IF NEW.giorno_saldo NOT IN (SELECT data FROM saldo_giornaliero) THEN
		INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES (new.giorno_saldo, 0, 0);
        END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER nuovo_costo_di_gestione
BEFORE INSERT
ON costi_di_gestione FOR EACH ROW
BEGIN
	IF NEW.giorno_saldo NOT IN (SELECT data FROM saldo_giornaliero) THEN
		INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES (new.giorno_saldo, 0, 0);
        END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER nuovo_stipendio
BEFORE INSERT
ON stipendio FOR EACH ROW
BEGIN
	IF NEW.giorno_saldo NOT IN (SELECT data FROM saldo_giornaliero) THEN
		INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES (new.giorno_saldo, 0, 0);
        END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER stipendio_update_saldo
AFTER INSERT
ON stipendio FOR EACH ROW
BEGIN
	UPDATE saldo_giornaliero set uscite = uscite + new.importo where saldo_giornaliero.data = new.giorno_saldo;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER costo_di_gestione_update_saldo
AFTER INSERT
ON costi_di_gestione FOR EACH ROW
BEGIN
	UPDATE saldo_giornaliero set uscite = uscite + new.importo where saldo_giornaliero.data = new.giorno_saldo;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER nuovo_prodotto
BEFORE INSERT
ON prodotto FOR EACH ROW
BEGIN
	IF NEW.categoria NOT IN (SELECT * FROM categoria) THEN
		INSERT INTO categoria(nome) VALUES (NEW.categoria);
        END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER ordine_update_quantità_prodotto
AFTER INSERT
ON composizione FOR EACH ROW
BEGIN
	UPDATE prodotto set quantità_disponibile = quantità_disponibile + new.quantità where codice_prod = new.codice_prod;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER ordine_update_saldo
AFTER INSERT
ON composizione FOR EACH ROW
BEGIN
	UPDATE saldo_giornaliero set uscite = uscite + ((SELECT prezzo_acquisto FROM prodotto WHERE codice_prod = new.codice_prod) * new.quantità) where saldo_giornaliero.data = new.giorno_saldo;
END$$
DELIMITER ;

INSERT INTO persona(nome, cognome, email, città) VALUES ("", "", "@@@", "");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile, password) VALUES (date(now()), 0, 0, NULL, "@@@", true, "adminpassword");
INSERT INTO turno(codice_dipendente, data, durata) VALUES (last_insert_id(), date(now()), "00:00:00");