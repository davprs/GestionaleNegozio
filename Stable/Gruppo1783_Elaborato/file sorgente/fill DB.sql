##### Inserire Video
INSERT INTO video(id_videocamera, data_inizio, data_fine, video_path) VALUES 
	(1, "2021-06-19 8:00:00", "2021-06-19 12:00:00", "C:\\Users\\crisa\\Desktop\\Progetto\ DB\\gestionalenegozio\\camera1-19-06-2021.mp4"),
	(2, "2021-06-19 8:00:00", "2021-06-19 12:00:00", "C:\\Users\\crisa\\Desktop\\Progetto\ DB\\gestionalenegozio\\camera2-19-06-2021.mp4");

##### Inserire Dipendenti
INSERT INTO persona(nome, cognome, email, città) VALUES ("Mario", "Rossi", "mariorossi@gmail.com", "Pescara");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile, password) VALUES ("2020-10-1", 1200, 80, NULL, "mariorossi@gmail.com", true, "pass");
INSERT INTO turno(codice_dipendente, data, durata) VALUES (LAST_INSERT_ID(), now(), "00:00:00");
INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("2020-10-1", NULL, "mariorossi@gmail.com");

INSERT INTO persona(nome, cognome, email, città) VALUES ("Gianni", "Mucci", "giannimucci@gmail.com", "Pescara");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile, password) VALUES ("2021-1-1", 1100, 90, NULL, "giannimucci@gmail.com", false, "pass");
INSERT INTO turno(codice_dipendente, data, durata) VALUES (LAST_INSERT_ID(), now(), "00:00:00");
INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("2021-1-1", NULL, "giannimucci@gmail.com");

INSERT INTO persona(nome, cognome, email, città) VALUES ("Luca", "Blu", "lucablu@gmail.com", "Montesilvano");
INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile, password) VALUES ("2029-11-1", 1300, 70, NULL, "lucablu@gmail.com", true, "pass");
INSERT INTO turno(codice_dipendente, data, durata) VALUES (LAST_INSERT_ID(), now(), "00:00:00");
INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("2019-11-1", NULL, "lucablu@gmail.com");



##### Inserire Prodotti
INSERT INTO prodotto (codice_prod, nome_prodotto, categoria, prezzo_vendita, prezzo_acquisto, quantità_disponibile) VALUES 
						(12345, "Lampadina Led 15W", "elettronica domestica", 8.2, 5, 50),
                        (16452, "Samsink TV 24'' AO824TV", "elettronica domestica", 259.99, 180, 50),
                        (25648, "TwoPlus Two", "telefonia", 499.99, 380 , 50),
                        (84685, "Duracell Batteria 9V", "elettronica domestica", 5.99, 2.5, 50),
                        (15346, "Bic JelPen 0.2", "cancelleria", 2.99, 1.2, 50);

##### Inserire Ricerca
INSERT INTO ricerca (id_ricerca, data, categoria, nome) VALUES 
	(NULL, "2021-06-19", "elettronica domestica", NULL),
    (NULL, "2021-06-18", "elettronica domestica", NULL),
    (NULL, "2021-06-18", "telefonia", NULL),
    (NULL, "2021-06-17", "telefonia", NULL),
    (NULL, "2021-06-17", "cancelleria", NULL),
    (NULL, "2021-06-19", "elettronica domestica", NULL);

##### Inserire Fornitore
INSERT INTO fornitore (nome) VALUES ("Giammarco Sars SRLS");
INSERT INTO fornitore (nome) VALUES ("Thomann SRLS");

INSERT INTO prodotto_di_fornitore(codice_prod, codice_fornitore) VALUES 
						(12345, 1),
                        (16452, 1),
                        (25648, 1),
                        (84685, 2),
                        (15346, 2);

##### Inserire Ordine
INSERT INTO ordine(codice_dipendente, giorno_saldo, codice_fornitore) VALUES (3, "2021-06-15", 1);
INSERT INTO composizione(codice_prod, quantità, codice_dipendente, giorno_saldo, codice_fornitore) VALUES 
						(12345, 20, 3, "2021-06-15", 1);

##### Inserire Vendita
INSERT INTO vendita (codice_scontrino, giorno_saldo, codice_dipendente, numero_cliente_tesserato) VALUES (NULL, "2021-6-22", 1, NULL);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità, prezzo_quando_venduto ) VALUES (LAST_INSERT_ID(), 12345, 1, 8.2);

UPDATE saldo_giornaliero set entrate = entrate + 
							(SELECT sum(prezzo_vendita * Q) from prodotto,
							(SELECT codice_prod C, quantità Q 
                            from prodotto_in_vendita
                            where codice_scontrino = LAST_INSERT_ID()) as proddd
                            where codice_prod = C)
							where data = "2021-6-22";

INSERT INTO vendita (codice_scontrino, giorno_saldo, codice_dipendente, numero_cliente_tesserato) VALUES (NULL, "2021-6-21", 2, NULL);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità, prezzo_quando_venduto) VALUES (LAST_INSERT_ID(), 12345, 2, 8.2);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità, prezzo_quando_venduto) VALUES (LAST_INSERT_ID(), 25648, 2, 499.99);
INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità, prezzo_quando_venduto) VALUES (LAST_INSERT_ID(), 84685, 2, 5.99);

UPDATE saldo_giornaliero set entrate = entrate + 
							(SELECT sum(prezzo_vendita * Q) from prodotto,
							(SELECT codice_prod C, quantità Q 
                            from prodotto_in_vendita
                            where codice_scontrino = LAST_INSERT_ID()) as proddd
                            where codice_prod = C)
							where data = "2021-6-21";

##### Inserire Turno
INSERT INTO turno(codice_dipendente, data, durata) VALUES (2, "2020-06-21 13:10:02", "8:10:50"); 

##### Inserire Costi_di_gestione
INSERT INTO costi_di_gestione(causale, breve_descrizione, importo, giorno_saldo, codice_dipendente) 
VALUES ("bolletta Emel", "Bolletta leggera", 100, "2021-06-19", 3);

##### Inserire Stipendio
INSERT INTO stipendio(codice_beneficiario, importo, giorno_saldo, codice_assegnatore)
VALUES (2, 1300, "2021-6-01", 3);

##### Inserire Fondo_cassa
INSERT INTO fondo_cassa (numero_cassa, importo, data_aggiornamento, codice_dipendente) VALUES
    (3, 210, NOW(), 2),
    (2, 210, NOW(), 2),
    (1, 210, NOW(), 2);