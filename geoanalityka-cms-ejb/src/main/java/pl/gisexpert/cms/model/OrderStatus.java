package pl.gisexpert.cms.model;

public enum OrderStatus {
	PENDING, // Płatność jest w trakcie rozliczenia.
	COMPLETED, // Płatność została zaakceptowana w całości. Środki są dostępne do wypłaty.
	CANCELED, // Płatność została anulowana. Płacący nie został obciążony 
	REJECTED // Płatność została odrzucona, ale płacący został obciążony
}
