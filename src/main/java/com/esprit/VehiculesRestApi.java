package com.esprit;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

 

import java.io.OutputStream;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class VehiculesRestApi {

    @RequestMapping(value = "/ontologies",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getontologies() {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator ontologiesIter = model.listOntologies();
            while (ontologiesIter.hasNext()) {
                Ontology ontology = (Ontology) ontologiesIter.next();

                JSONObject obj = new JSONObject();
                obj.put("name",ontology.getLocalName());
                obj.put("uri",ontology.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/classesList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getClasses() {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator classIter = model.listClasses();
            while (classIter.hasNext()) {
                OntClass ontClass = (OntClass) classIter.next();
                JSONObject obj = new JSONObject();
                obj.put("name",ontClass.getLocalName());
                obj.put("uri",ontClass.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/subClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getSubClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listSubClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                    JSONObject obj = new JSONObject();
                    obj.put("URI",sub.getURI());
                    obj.put("name",sub.getLocalName());
                    list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/Individus",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getIndividus() {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator individus = model.listIndividuals();
            while (individus.hasNext()) {
                Individual   sub = (Individual) individus.next();
                JSONObject obj = new JSONObject();
                obj.put("name",sub.getLocalName());
                obj.put("uri",sub.getURI());
                list.add(obj);

            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/superClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getSuperClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listSuperClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("URI",sub.getURI());
                list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getClasProperty",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getClasProperty(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);

            OntClass ontClass = model.getOntClass(classURI );
            Iterator subIter = ontClass.listDeclaredProperties();
            while (subIter.hasNext()) {
                OntProperty property = (OntProperty) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("propertyName",property.getLocalName());

                obj.put("propertyType",property.getRDFType().getLocalName());

                if(property.getDomain()!=null)
                    obj.put("domain", property.getDomain().getLocalName());
                if(property.getRange()!=null)
                    obj.put("range",property.getRange().getLocalName());

                list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/equivClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getequivClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listEquivalentClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("URI",sub.getURI());
                list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/Instances",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getInstancesClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listInstances();
        	JSONObject obj = new JSONObject();

            while (subIter.hasNext()) {
                Individual   sub = (Individual) subIter.next();
                StmtIterator it = sub.listProperties();

                while ( it.hasNext()) {
                       Statement s = (Statement) it.next();    

                    if (s.getObject().isLiteral()) {

                        obj.put("name",sub.getLocalName());
                        //Sobj.put("name",sub.getProperty(null));
                        obj.put("type",s.getPredicate().getLocalName());

                        obj.put("value",s.getLiteral().getLexicalForm().toString());

                        obj.put("uri",sub.getURI());
                        //System.out.println(""+s.getLiteral().getLexicalForm().toString()+" type = "+s.getPredicate().getLocalName());

                        }


                    else   {
                        obj.put("name",sub.getLocalName());
                        //Sobj.put("name",sub.getProperty(null));obj.put("value",s.getLiteral().getLexicalForm().toString());

                        obj.put("uri",sub.getURI());
                    } //System.out.println(""+s.getObject().toString().substring(53)+" type = "+s.getPredicate().getLocalName());


                         }
                System.out.println(sub);
                
                list.add(obj);

            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/isHierarchyRoot",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> isHirarchieroot(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );

          if (personne != null){
              JSONObject obj = new JSONObject();
              if (personne.isHierarchyRoot()){
                  obj.put("isroot","true");
              }else {
                  obj.put("isroot","false");
              }

              list.add(obj);

          }



            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> query() {
    	
		   
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String querygetPays =
   	    	     "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +        
   	    	     "PREFIX vec: <http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#>  " +
   	    	     "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +

   	    	     " SELECT ?MoyenneGamme ?nom " +
   	    	     " WHERE { ?MoyenneGamme vec:nom ?nom    } " ;
           
            //Query query = QueryFactory.create(req1);
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                //System.out.println(solution.get("x").toString());
                obj.put("IRI",solution.get("MoyenneGamme").toString());
                obj.put("property",solution.get("nom").toString());
                //obj.put("object",solution.get("z").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/getVehicules",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> queryGetallInstance() {
    	
		   
        List<JSONObject> list=new ArrayList();
        String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String querygetPays =
   	    	     "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +        
   	    	     "PREFIX vec: <http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#>  " +
   	    	     "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +

   	    	     " SELECT ?voiture  ?consomme ?fabriquant ?couleur ?nbporte" +
   	    	     " WHERE { ?voiture vec:consomme ?consomme .  ?voiture vec:est_fabrique_par ?fabriquant . ?voiture vec:couleur ?couleur . ?voiture vec:nombreDePortes ?nbporte .  } " ;
           
            //Query query = QueryFactory.create(req1);
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                //System.out.println(solution.get("x").toString());
                obj.put("id",x);

                obj.put("label",solution.get("voiture").toString().substring(solution.get("voiture").toString().indexOf('#')+1));
                //obj.put("type",solution.get("type").toString().substring(solution.get("type").toString().indexOf('#')+1));
                obj.put("consomme",solution.get("consomme").toString().substring(solution.get("consomme").toString().indexOf('#')+1));
                obj.put("fabriquePar ",solution.get("fabriquant").toString().substring(solution.get("fabriquant").toString().indexOf('#')+1));
                obj.put("couleur",solution.get("couleur").toString());
                obj.put("nombredePorte",solution.get("nbporte").toString().substring(0, 1));
                //obj.put("property",solution.get("nom").toString());
                //obj.put("object",solution.get("z").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getContents(File aFile) {
    	 //...checks on aFile are elided
    	 StringBuilder contents = new StringBuilder();
    	 try {
    	 //use buffering, reading one line at a time
    	 //FileReader always assumes default encoding is OK!
    	 BufferedReader input = new BufferedReader(new
    	FileReader(aFile));
    	 try {
    	 String line = null; //not declared within while loop
    	/*
    	 * readLine is a bit quirky :
    	* it returns the content of a line MINUS the newline.
    	* it returns null only for the END of the stream.
    	* it returns an empty String if two newlines appear in a
    	row.
    	 */
    	 while ((line = input.readLine()) != null) {
    	 contents.append(line);
    	contents.append(System.getProperty("line.separator"));
    	 }
    	 } finally {
    	 input.close();
    	 }
    	 } catch (IOException ex) {
    	 ex.printStackTrace();
    	 }
    	 return contents.toString();
    	 }

}

