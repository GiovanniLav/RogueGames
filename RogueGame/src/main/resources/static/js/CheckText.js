function controllaEta(eta){
	  if (isNaN(eta.value) || parseInt(eta.value)<0 || parseInt(eta.value) > 150)
	  {
	    alert('L\' età inserita non è corretta');
	    eta.value="";
	    eta.focus();
	  }
}

function controllaQuantita(quantita){
	  if (isNaN(quantita.value) || parseInt(quantita.value)<0)
	  {
	    alert('La quantità inserita non è corretta');
	    quantita.value="";
	    quantita.focus();
	  }
}

function controllaPrezzo(prezzo){
	  if (isNaN(prezzo.value) || prezzo.value<0)
	  {
	    alert('Il prezzo inserito non è corretto');
	    prezzo.value="";
	    prezzo.focus();
	  }
}