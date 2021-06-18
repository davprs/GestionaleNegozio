drop trigger nuova_vendita;

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

##### Inserire Dipendenti

INSERT INTO persona(nome, cognome, email, città) VALUES ("Mario", "Rossi", "mariorossi@gmail.com", "Pescara");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile) VALUES ("2020-10-1", 1200, 80, NULL, "mariorossi@gmail.com", false);
INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("2020-10-1", NULL, "mariorossi@gmail.com");

INSERT INTO persona(nome, cognome, email, città) VALUES ("Gianni", "Mucci", "giannimucci@gmail.com", "Pescara");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile) VALUES ("2021-1-1", 1100, 90, NULL, "giannimucci@gmail.com", false);
INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("2021-1-1", NULL, "giannimucci@gmail.com");

INSERT INTO persona(nome, cognome, email, città) VALUES ("Luca", "Blu", "lucablu@gmail.com", "Montesilvano");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile) VALUES ("2029-11-1", 1300, 70, NULL, "lucablu@gmail.com", true);
INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("2019-11-1", NULL, "lucablu@gmail.com");


##### Inserire Prodotti

INSERT INTO prodotto (codice_prod, nome_prodotto, categoria, prezzo_vendita, prezzo_acquisto, quantità_disponibile) VALUES 
						(12345, "Lampadina Led 15W", "elettronica domestica", 8.2, 5, 20),
                        (16452, "Samsink TV 24'' AO824TV", "elettronica domestica", 259.99, 180, 12),
                        (25648, "TwoPlus Two", "telefonia", 499.99, 380 , 20),
                        (84685, "Duracell Batteria 9V", "elettronica domestica", 5.99, 2.5, 30),
                        (15346, "Bic JelPen 0.2", "cancelleria", 2.99, 1.2, 35);




INSERT INTO fornitore (nome) VALUES ("Giammarco Sars SRLS");
INSERT INTO fornitore (nome) VALUES ("Thomann SRLS");

INSERT INTO prodotto_di_fornitore(codice_prod, codice_fornitore) VALUES 
						(12345, 1),
                        (16452, 1),
                        (25648, 1),
                        (84685, 2),
                        (15346, 2);

INSERT INTO ordine(codice_dipendente, giorno_saldo, codice_fornitore) VALUES (3, "2021-06-15", 1);
INSERT INTO composizione(codice_prod, quantità, codice_dipendente, giorno_saldo, codice_fornitore) VALUES 
						(12345, 20, 3, "2021-06-15", 1);


INSERT INTO video(id_videocamera, data_inizio, data_fine, video_path) VALUES 
	(1, "2021-06-15 8:00:00", "2021-06-15 12:00:00", "C:\\Users\\crisa\\Desktop\\Progetto\ DB\\gestionalenegozio\\Untitled.mp4"),
	(2, "2021-06-15 8:00:00", "2021-06-15 12:00:00", "C:\\Users\\crisa\\Desktop\\Progetto\ DB\\gestionalenegozio\\Untitled2.mp4");

##### Inserire Vendita
 #///datetime
#INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES ("2021-1-11", 0, 0);
INSERT INTO vendita (codice_scontrino, giorno_saldo, codice_dipendente, numero_cliente_tesserato) VALUES (NULL, "2021-5-28", 1, NULL);

INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) VALUES (LAST_INSERT_ID(), 12345, 1);


#////////////	SENZA DATA, va tolto (forse)
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) VALUES (LAST_INSERT_ID(), 12345, 2);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) VALUES (LAST_INSERT_ID(), 25648, 2);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) 
VALUES (LAST_INSERT_ID(), 84685, 2), (LAST_INSERT_ID(), 12345, 2), (LAST_INSERT_ID(), 12345, 2);

UPDATE saldo_giornaliero set entrate = entrate + 
							(SELECT sum(prezzo_vendita * Q) from prodotto,
							(SELECT codice_prod C, quantità Q 
                            from prodotto_in_vendita
                            where codice_scontrino = LAST_INSERT_ID()) as proddd
                            where codice_prod = C)
							where data = "2021-5-28";


INSERT INTO turno(codice_dipendente, data, durata) VALUES (2, "2020-02-01 13:10:02", "8:10:50"); 

INSERT INTO costi_di_gestione(causale, breve_descrizione, importo, giorno_saldo, codice_dipendente) 
VALUES ("bolletta 1", "Bolletta leggera", 100, "2021-05-30", 3);

INSERT INTO stipendio(codice_beneficiario, importo, giorno_saldo, codice_assegnatore)
VALUES (1, 1300, "2021-6-11", 3);

INSERT INTO fondo_cassa (numero_cassa, importo, data_aggiornamento, codice_dipendente) 
	VALUES(3, 210, NOW(), 2);