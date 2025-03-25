CREATE TABLE public.livro (
	id int8 NOT NULL,
	autores varchar(255) NOT NULL,
	datapublicacao date NOT NULL,
	editora varchar(255) NOT NULL,
	isbn varchar(255) NOT NULL,
	livrossemelhantes varchar(1000) NULL,
	titulo varchar(255) NOT NULL,
	CONSTRAINT livro_pkey PRIMARY KEY (id),
	CONSTRAINT uk_k8si93wtslp275pv65gity1gg UNIQUE (isbn)
);