package com.esprit;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

 

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
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
    List<JSONObject> listVoitures=new ArrayList();
    OntModel model = null;
    public OntModel readModel() {
    	String fileName = "data/vehicules.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
           
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
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
        try {
            this.model = this.readModel();
            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
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
        try {
            this.model = this.readModel();

            Iterator individus = this.model.listIndividuals();
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
        try {
            this.model = this.readModel();

            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
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
        try {
            this.model = this.readModel();

            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);

            OntClass ontClass = this.model.getOntClass(classURI );
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
        try {
            this.model = this.readModel();

            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
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
        try {
            this.model = this.readModel();

            String classURI = "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
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
        try {
            this.model = this.readModel();

            String querygetPays =
   	    	     "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +        
   	    	     "PREFIX vec: <http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#>  " +
   	    	     "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +

   	    	     " SELECT ?MoyenneGamme ?nom " +
   	    	     " WHERE { ?MoyenneGamme vec:nom ?nom    } " ;
           
            //Query query = QueryFactory.create(req1);
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, this.model);
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
    @RequestMapping(value = "/moyennegame",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getMoyenneGames() {
    	
		   
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
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);
              
                System.out.println(solution.get("nom").toString().substring(solution.get("nom").toString().indexOf('#')+1));
                obj.put("label",solution.get("MoyenneGamme").toString().substring(solution.get("MoyenneGamme").toString().indexOf('#')+1));
               obj.put("nom",solution.get("nom").toString().substring(solution.get("nom").toString().indexOf('#')+1));
        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "/hautegame",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getHauteGames() {
    	
		   
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

   	    	     " SELECT ?HauteGamme ?nom " +
   	    	     " WHERE { ?HauteGamme vec:nom ?nom    } " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);
              
                System.out.println(solution.get("nom").toString().substring(solution.get("nom").toString().indexOf('#')+1));
                obj.put("label",solution.get("HauteGamme").toString().substring(solution.get("HauteGamme").toString().indexOf('#')+1));
               obj.put("nom",solution.get("nom").toString().substring(solution.get("nom").toString().indexOf('#')+1));
        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/getMarques",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getMarques() {
    	
		   
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

   	    	     " SELECT DISTINCT ?Marque  " +
   	    	     " WHERE { ?Marque vec:nom ?nom    } " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);
              
                System.out.println(solution.get("Marque").toString().substring(solution.get("Marque").toString().indexOf('#')+1));
                obj.put("marque",solution.get("Marque").toString().substring(solution.get("Marque").toString().indexOf('#')+1));
             //  obj.put("nom",solution.get("nom").toString().substring(solution.get("nom").toString().indexOf('#')+1));
        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/getCarburants",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getCarburants() {
    	
		   
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

				" SELECT DISTINCT ?Carburant  " +
				" WHERE { ?Carburant rdfs:subClassOf vec:Carburant} " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);           
                obj.put("carburant",solution.get("Carburant").toString().substring(solution.get("Carburant").toString().indexOf('#')+1));

        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/getCylindress",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getCylindres() {
    	
		   
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

 				" SELECT ?Cylindree" +
 				" WHERE {  ?Cylindree rdfs:subClassOf vec:Cylindree} " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);           
                obj.put("Cylindree",solution.get("Cylindree").toString().substring(solution.get("Cylindree").toString().indexOf('#')+1));

        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/vehicule/{couleur}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getVecByCouleur(@PathVariable String couleur) {
    	
		   
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

  					" SELECT ?voiture  " +
  					" WHERE { ?voiture vec:couleur '"+couleur+"'} " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);           
                obj.put("label",solution.get("voiture").toString().substring(solution.get("voiture").toString().indexOf('#')+1));

        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/vehicules/{consumme}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getVecByConsumme(@PathVariable String consumme) {
    	
		   
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

  					" SELECT ?voiture  " +
  					" WHERE {  ?consomme vec:consomme '"+consumme+"'} " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
                System.out.println(solution);           
                obj.put("label",solution.get("voiture").toString().substring(solution.get("voiture").toString().indexOf('#')+1));

        
          
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/getConsommes",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getConsommes() {
    	
		   
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
            	    	     " SELECT   DISTINCT  ?consomme " +
    		   	    	     " WHERE { ?voiture vec:consomme  ?consomme} " ;
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("id",x);
              
                obj.put("label",solution.get("consomme").toString().substring(solution.get("consomme").toString().indexOf('#')+1));

                
        
          
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
        try {
            this.model = this.readModel();

            String querygetPays =
   	    	     "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +        
   	    	     "PREFIX vec: <http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#>  " +
   	    	     "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +

   	    	     " SELECT ?voiture  ?consomme ?fabriquant ?couleur ?nbporte" +
   	    	     " WHERE { ?voiture vec:consomme ?consomme .  ?voiture vec:est_fabrique_par ?fabriquant . ?voiture vec:couleur ?couleur . ?voiture vec:nombreDePortes ?nbporte .  } " ;
           
            //Query query = QueryFactory.create(req1);
            QueryExecution qe = QueryExecutionFactory.create(querygetPays, this.model);
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
            listVoitures = list ;
            System.out.println(x);
            return listVoitures;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/add",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> AddInstance(@RequestParam("name") String name,@RequestParam("couleur") String couleur,@RequestParam("nbPorte") int nbPorte ,
    		@RequestParam("marque") String marque,@RequestParam("cylindree") String cylindree,@RequestParam("boite") String boite,@RequestParam("type") String type,@RequestParam("consomme") String consomme) {
    	
		 String NS= "http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#";
        List<JSONObject> list=new ArrayList();
        try {
            this.model = this.readModel();

         OntClass classe = this.model.getOntClass(NS+type);

         // create individual ex:jack
         Individual ind = this.model.createIndividual( NS + name, classe );

         // create some properties - probably better to use FOAF here really
         DatatypeProperty couleurP = this.model.getDatatypeProperty(NS + "couleur" );
         DatatypeProperty nbPorteP = this.model.getDatatypeProperty( NS + "nombreDePortes" );
         ObjectProperty marqueP = this.model.getObjectProperty(NS + "est_fabrique_par");
         ObjectProperty consommeP = this.model.getObjectProperty(NS + "consomme");
         ObjectProperty composeP = this.model.getObjectProperty(NS + "est_compose_de");

         HasValueRestriction marqueR =
        		 this.model.createHasValueRestriction( marque, marqueP, ind );
         HasValueRestriction consommeR =
        		 this.model.createHasValueRestriction( consomme, consommeP, ind );
         HasValueRestriction composeR =
        		 this.model.createHasValueRestriction( boite, composeP, ind );
         HasValueRestriction compose1R =
        		 this.model.createHasValueRestriction( cylindree, composeP, ind );
         IntersectionClass ukIndustrialConf =
        		 this.model.createIntersectionClass( NS + "name",
        				 this.model.createList( new RDFNode[] {marqueR, consommeR,composeR,compose1R} ) );
         ind.addProperty( couleurP, this.model.createLiteral( couleur ) )
             .addProperty( nbPorteP, this.model.createTypedLiteral( nbPorte) );
         
         String output = "<!-- http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#clio -->\r\n"
         		+ "\r\n"
         		+ "    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#"+name+"\">\r\n"
         		+ "        <rdf:type rdf:resource=\"http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#"+type+"\"/>\r\n"
         		+ "        <vehicules:consomme rdf:resource=\"http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#"+consomme+"\"/>\r\n"
         		+ "        <vehicules:est_compose_de rdf:resource=\"http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#"+boite+"\"/>\r\n"
         		+ "        <vehicules:est_compose_de rdf:resource=\"http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#"+cylindree+"\"/>\r\n"
         		+ "        <vehicules:est_fabrique_par rdf:resource=\"http://www.semanticweb.org/nader/ontologies/2020/10/vehicules#"+marque.toUpperCase()+"\"/>\r\n"
         		+ "        <vehicules:couleur>"+couleur+"</vehicules:couleur>\r\n"
         		+ "        <vehicules:nombreDePortes rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">"+nbPorte+"</vehicules:nombreDePortes>\r\n"
         		+ "    </owl:NamedIndividual>\r\n"
         		+ "";
         Path path = Paths.get("data/vehicules.owl");
         List<String> lines = Files.readAllLines(path);
         lines.add(12, output); // index 3: between 3rd and 4th line
         Files.write(path, lines);
         System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
}

