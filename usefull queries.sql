##### Visualizza tutti dati personali Dipendente

select  nome, cognome, data_assunzione, stipendio_base, ore_lavoro_mensili, T.tot_ore_mensili, è_responsabile 
from Persona P, dipendente D , (select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili 
            from turno  
            where data between "2021-04-24" and "2021-05-30" 
            group by codice_dipendente) as T
where P.email = D.email AND T.codice_dipendente = D.codice_dipendente;
select * from Persona P;

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
select * from composizione;
select * from ricerca;
select * from stipendio;

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
            where data between "2021-04-24" and "2021-05-30" 
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

select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata)))as tot_ore_mensili from turno where data between "2021-04-24" and "2021-05-25"  group by codice_dipendente;
delete from turno where codice_dipendente=1;
