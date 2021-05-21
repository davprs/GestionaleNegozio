##### Visualizza tutti dati personali Dipendente

select * from Persona P, dipendente D where P.email = D.email;
select * from Persona P;

##### Visualizza tutti i responsabili
#select * from Persona P, dipendente D where D.è_responsabile is true AND P.email = D.dati_personali

#### Visualizza prodotti

select * from prodotto;

#	visualizza statistiche su quanto è il guadagno e la percentuale di guadagno
#select @valore_vendita:=sum(prezzo_vendita*quantità_disponibile) as valore_vendita, 
#		@spesa_acquisto:=sum(prezzo_acquisto*quantità_disponibile) as spesa_acquisto,
#		@guadagno:=cast(@valore_vendita - @spesa_acquisto as Decimal(10, 2)) as guadagno,
#        cast(@guadagno/ @spesa_acquisto *100 as Decimal(6, 3)) as perc_guad
#        from prodotto;

select * from categoria;
select * from vendita;
select * from saldo_giornaliero;
select * from prodotto_in_vendita;

select * from prodotto_in_vendita PV, prodotto P where P.codice_prod = PV.codice_prod;

select * from turno;
delete from turno where codice_dipendente=1;
UPDATE turno SET durata=0.29 WHERE codice_dipendente = 1 AND durata IS NULL LIMIT 1;
