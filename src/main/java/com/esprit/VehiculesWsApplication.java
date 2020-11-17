package com.esprit;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

@SpringBootApplication
public class VehiculesWsApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(VehiculesWsApplication.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {
		String fileName = "data/vehicules.owl";
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);
			OntModel model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_DL_MEM);
			model.read(reader,null);
			model.write(System.out,"RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
