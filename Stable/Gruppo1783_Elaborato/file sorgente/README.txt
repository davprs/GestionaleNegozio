Il programma ha bisogno di essere lanciato da una 
directory contenente un file chiamato “config” 
("Scene test fx\config") che contiene le 
informazioni necessarie a connettersi al DB. 

Quindi se fosse necessario modificare indirizzo, 
nome utente o password del DB, quel file dovrà 
essere aggiornato di conseguenza.

Il file necessario a creare il DB si chiama:
 - "db_gestionale.sql"
Ed è presente in questa cartella.

Per garantire una installazione rapida ed 
user-friendly durante il primo avvio del programma è 
necessario effettuare il login utilizzando le 
credenziali : 
 - codice dipendente : 1
 - password : adminpassword
Una volta eseguito il login presso questo account 
fittizio è possibile registrare altri dipendenti 
tramite la scheda :
 - “Alti Privilegi” > “Gestisci personale”. 

E’ consigliato registrare almeno un nuovo account 
Responsabile, effettuare il logout, effettuare 
il login con le credenziali del nuovo account, 
andare nuovamente in :
 - “Alti Privilegi” > “Gestisci personale”, 
selezionare la riga della tabella che non ha nè nome 
nè cognome,  premere “Mostra info”, premere su 
“Licenzia”.



Nel caso si voglia provare l’applicazione con alcuni 
dati già inseriti nel DB, è presente un file chiamato :
"fill DB.sql" che contiene delle istruzioni per popolare
il DB.

Nelle prime righe del file "fill DB.sql" è presente un 
path non valido per inserire nel DB il path dei 2 video
presenti in questa cartella "camera______.mp4".

Per non avere problemi durante la riproduzione dei
suddetti video durante la navigazione all interno dell
applicazione si consiglia di aggiornare i percorsi in 
questione.
