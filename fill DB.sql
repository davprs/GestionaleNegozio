#drop trigger dopo_nuovo_prodotto_in_vendita;
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
	IF NEW.data NOT IN (SELECT data FROM saldo_giornaliero) THEN
		INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES (new.data, 0, 0);
        END IF;
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


##### Inserire Vendita
 #///datetime
#INSERT INTO saldo_giornaliero(data, entrate, uscite) VALUES ("2021-1-11", 0, 0);
INSERT INTO vendita (codice_scontrino, data, giorno_saldo, codice_dipendente, numero_cliente_tesserato) VALUES (NULL, "2022-1-11", "2022-1-11", 1, NULL);


#////////////	SENZA DATA, va tolto (forse)
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) VALUES (LAST_INSERT_ID(), 12345, 2);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) VALUES (LAST_INSERT_ID(), 25648, 2);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità) VALUES (LAST_INSERT_ID(), 84685, 2);

UPDATE saldo_giornaliero set entrate = entrate + 
							(SELECT sum(prezzo_vendita * Q) from prodotto,
							(SELECT codice_prod C, quantità Q 
                            from prodotto_in_vendita
                            where codice_scontrino = LAST_INSERT_ID()) as proddd
                            where codice_prod = C)
							where data = "2021-1-11";



