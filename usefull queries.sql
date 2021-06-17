##### Visualizza tutti dati personali Dipendente

select  nome, cognome, data_assunzione, stipendio_base, ore_lavoro_mensili, T.tot_ore_mensili, è_responsabile 
from Persona P, dipendente D , (select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili 
            from turno  
            where data between "2021-04-24" and "2021-05-30" 
            group by codice_dipendente) as T
where P.email = D.email AND T.codice_dipendente = D.codice_dipendente;
select * from Persona P;
select * from turno;

##### Visualizza tutti i responsabili
#select * from Persona P, dipendente D where D.è_responsabile is true AND P.email = D.dati_personali

#### Visualizza prodotti

select * from fornitore;
select * from ordine;

select nome_prodotto, prezzo_acquisto from prodotto where codice_prod = 12345;

#	visualizza statistiche su quanto è il guadagno e la percentuale di guadagno
#select @valore_vendita:=sum(prezzo_vendita*quantità_disponibile) as valore_vendita, 
#		@spesa_acquisto:=sum(prezzo_acquisto*quantità_disponibile) as spesa_acquisto,
#		@guadagno:=cast(@valore_vendita - @spesa_acquisto as Decimal(10, 2)) as guadagno,
#        cast(@guadagno/ @spesa_acquisto *100 as Decimal(6, 3)) as perc_guad
#        from prodotto;

select * from categoria;
select * from vendita;
select * from prodotto;
select * from saldo_giornaliero;
select * from prodotto_in_vendita;
select * from prodotto_di_fornitore;
select * from fornitore;
select * from ordine;
select * from turno;
delete from stipendio where codice_beneficiario > 3;
delete from turno where codice_dipendente > 3;
delete from dipendente where codice_dipendente > 3;
delete from composizione where codice_prod = 34567;

(SELECT PV.codice_prod, P.nome_prodotto, P.prezzo_vendita as prezzo, PV.quantità, V.numero_cliente_tesserato, J.nome, J.cognome 
	FROM prodotto_in_vendita PV, vendita V, prodotto P, (
		select nome, cognome from persona P , cliente_tesserato CT, vendita V 
			WHERE P.email = CT.email AND CT.numero_tessera = V.numero_cliente_tesserato AND V.codice_scontrino =1) as J 
    WHERE PV.codice_scontrino = V.codice_scontrino AND PV.codice_prod = P.codice_prod AND V.codice_scontrino =1)
    UNION
    (SELECT PV.codice_prod, P.nome_prodotto, P.prezzo_vendita as prezzo, PV.quantità, null, null, null 
	FROM prodotto_in_vendita PV, vendita V, prodotto P 
    WHERE PV.codice_scontrino = V.codice_scontrino AND PV.codice_prod = P.codice_prod AND V.codice_scontrino =1) limit 1;


select nome, cognome from persona P , cliente_tesserato CT WHERE P.email = CT.email AND CT.numero_tessera = 21;

select * from dipendente;

select * from composizione;
select * from ricerca;
select * from stipendio;
select * from video;
select * from cliente_tesserato;
select * from persona;
select * from dipendente;

SELECT * FROM fornitore;
SELECT F.nome, P.nome_prodotto, P.prezzo_acquisto FROM fornitore F, prodotto P, prodotto_di_fornitore PF WHERE F.codice_fornitore = PF.codice_fornitore AND P.codice_prod = PF.codice_prod;

delete from persona where email = "marcelloascani@gmail.com";
delete from dipendente where email = "marcelloascani@gmail.com";

SELECT nome, cognome, P.email, città from persona P where P.email = "ciao@sonodavide.com";
SELECT PV.codice_prod, P.nome_prodotto, P.prezzo_vendita as prezzo, PV.quantità FROM prodotto_in_vendita PV, vendita V, prodotto P WHERE PV.codice_scontrino = V.codice_scontrino AND PV.codice_prod = P.codice_prod AND V.codice_scontrino = 9;

SELECT P.nome_prodotto, P.prezzo_vendita as prezzo, PV.quantità 
	FROM prodotto P, prodotto_in_vendita PV 
    WHERE PV.codice_scontrino = 1;
    
SELECT V.codice_scontrino, PV.codice_prod, P.prezzo_vendita as prezzo, PV.quantità FROM prodotto_in_vendita PV, vendita V, prodotto P where PV.codice_scontrino = V.codice_scontrino AND PV.codice_prod = P.codice_prod AND V.codice_scontrino = 9;


SELECT nome, cognome, P.email, città, C.numero_tessera from persona P, cliente_tesserato C 
	where P.email = C.email AND P.email = "ciao@sonodavide.com";		

select P.nome, P.cognome, P.email, P.città from persona P, cliente_tesserato C where P.email = C.email AND C.numero_tessera = 4;
delete from cliente_tesserato where numero_tessera = 8;
update persona set nome = "dav" where email = "ciao@sonodavide.com" ; 
select id_videocamera, data_inizio, timediff(data_fine, data_inizio) as durata, video_path from video where valido = true;

UPDATE video SET valido = false WHERE id_videocamera = 1 AND data_inizio = "2021-06-15 8:00:00";

select SUM(uscite) from saldo_giornaliero where data between "2021-5-28" and "2021-6-30";
select * from saldo_giornaliero;
select ROUND(SUM(entrate) - SUM(uscite), 2) from saldo_giornaliero where data between "2021-5-28" and "2021-6-29";
select * from costi_di_gestione;

select * from prodotto_in_vendita PV, prodotto P where P.codice_prod = PV.codice_prod;

select * from turno;
select nome, cognome, ore_lavoro_mensili, T.tot_ore_mensili, Lavoro.sta_lavorando 
		from Persona P, dipendente D , (
			select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili 
            from turno  
            where data between "2021-04-24" and "2021-06-30" 
            group by codice_dipendente) as T, 
            (
            select codice_dipendente as CD, EXISTS(select * from turno where durata is null and CD = codice_dipendente) as sta_lavorando
                from turno
                group by codice_dipendente
            ) AS Lavoro
where P.email = D.email AND D.codice_dipendente = T.codice_dipendente AND D.codice_dipendente = Lavoro.CD
group by D.codice_dipendente;

select codice_dipendente as CD, EXISTS(select * from turno where durata is null and CD = codice_dipendente) as sta_lavorando
                from turno
                group by codice_dipendente;

select * from stipendio;
select codice_beneficiario, max(giorno_saldo) as ultimo_stipendio, importo from stipendio group by codice_beneficiario;

select codice_beneficiario, giorno_saldo, importo
from stipendio where (codice_beneficiario, giorno_saldo) in (
    select codice_beneficiario, max(giorno_saldo) as giorno_saldo
    from stipendio
    group by codice_beneficiario
);
select  nome, cognome, data_assunzione, ore_lavoro_mensili, T.tot_ore_mensili, stipendio_base, è_responsabile, D.codice_dipendente 
		from Persona P, dipendente D , (select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili 
						            from turno 
						            where data between "2021-05-16" and "2021-06-17"
						            group by codice_dipendente) as T 
						where P.email = D.email AND T.codice_dipendente = D.codice_dipendente ;
SELECT nome, cognome, data_assunzione, ore_lavoro_mensili, tot_ore_mensili, stipendio_base, è_responsabile, giorno_saldo, importo
FROM 
	(select  nome, cognome, data_assunzione, ore_lavoro_mensili, T.tot_ore_mensili, stipendio_base, è_responsabile, D.codice_dipendente 
		from Persona P, dipendente D , (select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili 
						            from turno 
						            where data between "2021-05-16" and "2021-06-17"
						            group by codice_dipendente) as T 
						where P.email = D.email AND T.codice_dipendente = D.codice_dipendente ) as R
                        
	LEFT OUTER JOIN (select codice_beneficiario, giorno_saldo, importo
							from stipendio where (codice_beneficiario, giorno_saldo) in ( 
							select codice_beneficiario, max(giorno_saldo) as giorno_saldo 
							from stipendio 
							group by codice_beneficiario)) as LS 
	ON R.codice_dipendente = LS.codice_beneficiario;


select codice_dipendente, SEC_TO_TIME(SUM(stipendioTIME_TO_SEC(durata)))as tot_ore_mensili from turno where data between "2021-04-24" and "2021-07-25"  group by codice_dipendente;
delete from turno where codice_dipendente=1;
