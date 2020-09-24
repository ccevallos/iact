	Dropzone.prototype.defaultOptions.dictDefaultMessage = "Arrastre y suelte aquí los archivos a cargar";
	Dropzone.prototype.defaultOptions.dictFallbackMessage = "Su navegador no soporta arrastrar y soltar archivos para carga.";
	Dropzone.prototype.defaultOptions.dictFallbackText = "Por favor use Please use el formulario siguiente para cargar sus archivos.";
	Dropzone.prototype.defaultOptions.dictFileTooBig = "Archivo demasiado grande {{filesize}} MB. Máximo permitido: {{maxFilesize}} MB.";
	Dropzone.prototype.defaultOptions.dictInvalidFileType = "Tu no puedes cargar archivos de este tipo.";
	Dropzone.prototype.defaultOptions.dictResponseError = "El servidor responde con código {{statusCode}}.";
	Dropzone.prototype.defaultOptions.dictCancelUpload = "Carga cancelada";
	Dropzone.prototype.defaultOptions.dictCancelUploadConfirmation = "¿Esta seguro que desea cancelar la carga?";
	Dropzone.prototype.defaultOptions.dictRemoveFile = "Eliminar el archivo";
	Dropzone.prototype.defaultOptions.dictMaxFilesExceeded = "No se pueden cargar más archivos.";

	exito=function(file, response){
			location.reload(true);
	    }

	Dropzone.options.fileUpload = {
		autoProcessQueue : false,
		uploadMultiple: true,
		addRemoveLinks : true,
		maxFilesize : 5, // MB
		acceptedFiles:".zip,.csv",
		success: exito
	}
	
	preprocesa=function(nombre){
	  $.ajax({url: "Preprocesa.jsx?nombre="+nombre, 
              success: exito,
			  timeout:0});
	  $(".loader").show();
	};

	$(window).on('load',function() {
	    $(".loader").hide();
	});
	