-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.1              
-- * Generator date: Dec  4 2018              
-- * Generation date: Wed May 19 15:32:13 2021 
-- * LUN file: C:\Users\crisa\Desktop\Progetto DB\DB negozio.lun 
-- * Schema: Schema rel/1 
-- ********************************************* 


-- Database Section
-- ________________ 

create database DB_prog;
use DB_prog;


-- Tables Section
-- _____________ 

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
     codice_fornitore int not null,
     codice_dipendente int not null,
     data date not null,
     codice_prod int not null,
     quantità int not null,
     constraint IDComposizione primary key (codice_prod, codice_fornitore, codice_dipendente, data));

create table Costi_di_gestione (
     id_costo int not null auto_increment,
     causale varchar(50) not null,
     breve_descrizione varchar(200) not null,
     data date not null,
     importo float(1) not null,
     giorno_saldo date not null,
     codice_dipendente int not null,
     constraint IDCosti_di_gestione primary key (id_costo));

create table Dipendente (
     data_assunzione date not null,
     stipendio_base float(1) not null,
     ore_lavoro_mensili float(1) not null,
     codice_dipendente int not null auto_increment,
     email varchar(40) not null,
     è_responsabile char not null,
     constraint IDDipendente primary key (codice_dipendente),
     constraint FKdipendente_mail_ID unique (email));

create table Fornitore (
     codice_fornitore int not null auto_increment,
     codice_prod int,
     nome varchar(30) not null,
     constraint IDFornitore primary key (codice_fornitore),
     constraint FKvende_ID unique (codice_prod));

create table Ordine (
     codice_dipendente int not null,
     codice_fornitore int not null,
     data date not null,
     giorno_saldo date not null,
     constraint IDOrdine_ID primary key (codice_fornitore, codice_dipendente, data));

create table Persona (
     nome varchar(15) not null,
     cognome varchar(15) not null,
     email varchar(40) not null,
     città varchar(40) not null,
     constraint IDPersona_ID primary key (email));

create table Prodotto (
     codice_prod int not null,
     nome_prodotto varchar(40) not null,
     prezzo_vendita DECIMAL(6, 2) not null,
     prezzo_acquisto DECIMAL(6, 2) not null,
     quantità_disponibile int not null,
     categoria varchar(40) not null,
     constraint IDProdotto_ID primary key (codice_prod));

create table Prodotto_in_Vendita (
     codice_scontrino int not null,
     codice_prod int not null,
     quantità int not null,
     constraint IDProdotto_in_Vendita primary key (codice_prod, codice_scontrino));

create table Ricerca (
     id_ricerca int not null auto_increment,
     data date not null,
     categoria varchar(20),
     nome varchar(100),
     constraint IDRicerca primary key (id_ricerca));

create table Saldo_giornaliero (
     data date not null,
     entrate float(1) not null,
     uscite float(1) not null,
     constraint IDSaldo_giornaliero primary key (data));

create table Stipendio (
     codice_beneficiario int not null auto_increment,
     importo float(1) not null,
     data date not null,
     giorno_saldo date not null,
     codice_assegnatore int not null,
     constraint IDStipendio primary key (codice_beneficiario, data));

create table Turno (
     codice_dipendente int not null auto_increment,
     data date not null,
     durata float(1),
     constraint IDTurno primary key (codice_dipendente, data));

create table Vendita (
     data date not null,
     codice_scontrino int not null auto_increment,
     giorno_saldo date not null,
     codice_dipendente int not null,
     numero_cliente_tesserato int,
     constraint IDVendita_ID primary key (codice_scontrino, data));

create table Video (
     id_videocamera int not null,
     data_inizio date not null,
     data_fine date not null,
     video_path varchar(100) not null,
     nome varchar(50),
     valido bool default true not null,
     constraint IDVideo primary key (id_videocamera, data_inizio));


-- Constraints Section
-- ___________________ 

alter table Cliente_Tesserato add constraint FKcliente_mail_FK
     foreign key (email)
     references Persona (email);

alter table Composizione add constraint FKprodotto
     foreign key (codice_prod)
     references Prodotto (codice_prod);

alter table Composizione add constraint FKordine
     foreign key (codice_fornitore, codice_dipendente, data)
     references Ordine (codice_fornitore, codice_dipendente, data);

alter table Costi_di_gestione add constraint FKgestione_compone 
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Costi_di_gestione add constraint FKgestione_inserisce
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Dipendente add constraint FKdipendente_mail_FK
     foreign key (email)
     references Persona (email);

alter table Fornitore add constraint FKvende_FK
     foreign key (codice_prod)
     references Prodotto (codice_prod);

-- Not implemented
-- alter table Ordine add constraint IDOrdine_CHK
--     check(exists(select * from Composizione
--                  where Composizione.codice_fornitore = codice_fornitore and Composizione.codice_dipendente = codice_dipendente and Composizione.data = data)); 

alter table Ordine add constraint FKordine_compone 
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Ordine add constraint FKordine_riceve
     foreign key (codice_fornitore)
     references Fornitore (codice_fornitore);

alter table Ordine add constraint FKordine_sottomette
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

-- Not implemented
-- alter table Persona add constraint IDPersona_CHK
--     check(exists(select * from Cliente_Tesserato
--                  where Cliente_Tesserato.email = email)); 

-- Not implemented
-- alter table Prodotto add constraint IDProdotto_CHK
--     check(exists(select * from Fornitore
--                  where Fornitore.codice_prod = codice_prod)); 

alter table Prodotto add constraint FKappartiene
     foreign key (categoria)
     references Categoria (nome);

alter table Prodotto_in_Vendita add constraint FKprod_in_vendita_è
     foreign key (codice_prod)
     references Prodotto (codice_prod);

alter table Prodotto_in_Vendita add constraint FKprod_in_vendita_vende
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

-- Not implemented
-- alter table Vendita add constraint IDVendita_CHK
--     check(exists(select * from Prodotto_in_Vendita
--                  where Prodotto_in_Vendita.codice_scontrino = data)); 

alter table Vendita add constraint FKvendita_compone 
     foreign key (giorno_saldo)
     references Saldo_giornaliero (data);

alter table Vendita add constraint FKvendita_effettua
     foreign key (codice_dipendente)
     references Dipendente (codice_dipendente);

alter table Vendita add constraint FKvendita_partecipa
     foreign key (numero_cliente_tesserato)
     references Cliente_Tesserato (numero_tessera);


-- Index Section
-- _____________ 

