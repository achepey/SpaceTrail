package javagame;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.Serializable;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

/**
 * Created by Robert on 11/28/2014.
 XML Structure:
 <game>
    <ship>
        ...ship stuff
    </ship>
    <resources>
        ...resources stuff
    </resources>
    <people>
        <person0>
            ...person stuff
        </person0>
        <person1>

        </person1>
        ...to person4
    </people>
    <destinationPlanet></destinationPlanet>
    <distance></distance>
    <money></money>
    <previousPlanet></previousPlanet>
    <pace></pace>
    <totalDistance></totalDistance>
 </game>

 */
public class GameFileLoader implements Serializable
{
    Document doc;
    NodeList game_nodes;
    Game game;

    //opens given file to read
    public GameFileLoader(String fileName) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(fileName);
            game_nodes = doc.getChildNodes();
            Node gameNode = getNode("game", game_nodes);
            game_nodes = gameNode.getChildNodes();

            game = new Game();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public GameFileLoader(File file) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(file);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);

            Source schemaFile = new StreamSource(new File("mySchema.xsd"));
            Schema schema = factory.newSchema(schemaFile);

            Validator validator = schema.newValidator();
            try {
                validator.validate(new DOMSource(doc));
            } catch (SAXException e) {
                    // instance document is invalid!
            }

            game_nodes = doc.getChildNodes();
            Node gameNode = getNode("game", game_nodes);
            game_nodes = gameNode.getChildNodes();

            game = new Game();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Loads data from XML into game object
    public Game loadGame() {
        loadShip();
        loadResources();
        loadPeople();
        String destinationPlanet = getNodeValue("destinationPlanet", game_nodes);
        game.setFirstDestination(destinationPlanet);
        game.setDistanceRemaining(Double.parseDouble(getNodeValue("distance", game_nodes)));
        game.setMoney(Integer.parseInt(getNodeValue("money", game_nodes)));
        String previousPlanet = getNodeValue("previousPlanet", game_nodes);
        game.setPrevious(previousPlanet);
        if(!previousPlanet.equals("Temp")) {
            game.setFirstMove(false);
        }
        int pace = Integer.parseInt(getNodeValue("pace", game_nodes));
        game.setTotalDistance(Double.parseDouble(getNodeValue("totalDistance", game_nodes)));
        game.setSpeed(pace);
        String visitedPlanetsString = getNodeValue("visitedPlanets", game_nodes);
        String delims = "[ ]";
        String[] visitedPlanetsArray = visitedPlanetsString.split(delims);
        for(int i = 0; i < visitedPlanetsArray.length; ++i) {
            game.setVisited(visitedPlanetsArray[i]);
        }
        Node crewNames = getNode("peopleNames", game_nodes);
        NodeList crewNamesNodes = crewNames.getChildNodes();
        for(int i = 0; i < 5; ++i) {
            game.crewNames.add(getNodeValue("crew"+i,crewNamesNodes));
        }
        System.out.println("crew names:");
        for(String s: game.crewNames) {
            System.out.println(s + "\n");
        }

        return game;
    }
    //Loads ship data into game object
    private void loadShip() {
        Node ship = getNode("ship", game_nodes);
        NodeList ship_child_nodes = ship.getChildNodes();
        game.getShip().setHullStatus(Integer.parseInt(getNodeValue("hull", ship_child_nodes)));
        game.getShip().setEngineStatus(Integer.parseInt(getNodeValue("engine",ship_child_nodes)));
        game.getShip().setWingStatus(Integer.parseInt(getNodeValue("wing",ship_child_nodes)));
        game.getShip().setLivingBayStatus(Integer.parseInt((getNodeValue("livingBay",ship_child_nodes))));
        game.getShip().setXpos(Float.parseFloat(getNodeValue("xpos",ship_child_nodes)));
    }
    //Loads resource data into game object
    private void loadResources() {
        Node resources = getNode("resources", game_nodes);
        NodeList resources_child_nodes = resources.getChildNodes();
        //Resources
        game.getResources().setFood(Integer.parseInt(getNodeValue("food", resources_child_nodes)));
        game.getResources().setFuel(Integer.parseInt(getNodeValue("fuel", resources_child_nodes)));
        game.getResources().setCompound(Integer.parseInt(getNodeValue("compound", resources_child_nodes)));
        game.getResources().setAluminum(Integer.parseInt(getNodeValue("aluminum", resources_child_nodes)));

        //Spare parts
        int spareEngines, spareWings, spareLivingBays;
        spareEngines = Integer.parseInt(getNodeValue("spareEngines",resources_child_nodes));
        spareWings = Integer.parseInt(getNodeValue("spareWings", resources_child_nodes));
        spareLivingBays = Integer.parseInt(getNodeValue("spareLivingBays", resources_child_nodes));
        for(int i = 0; i < spareEngines; ++i) {
            game.getResources().addSpare(new Part("engine", 100));
        }
        for(int i = 0; i < spareWings; ++i) {
            game.getResources().addSpare(new Part("wing", 100));
        }
        for(int i = 0; i < spareLivingBays; ++i) {
            game.getResources().addSpare(new Part("livingBay", 100));
        }
    }
    //Loads people object into game object
    private void loadPeople() {
        Node people = getNode("people", game_nodes);
        NodeList people_child_nodes = people.getChildNodes();
        //People
        Race loadRace = new Race();
        for(int i = 0; i < people_child_nodes.getLength(); ++i) {
            Node person = getNode("person"+i, people_child_nodes);
            NodeList person_nodes = person.getChildNodes();
            if(i == 0) {
                game.addCrew(getNodeValue("name",person_nodes), true);
                //this only needs to happen once; it is not reliant on captain
                loadRace.setName(getNodeValue("raceName", person_nodes));
                loadRace.setStrength(getNodeValue("raceStrength", person_nodes));
                loadRace.setWeakness(getNodeValue("raceWeakness", person_nodes));
            }
            else {
                game.addCrew(getNodeValue("name",person_nodes), false);
            }
            game.getCrew(i).setAge(Integer.parseInt(getNodeValue("age", person_nodes)));
            game.getCrew(i).setCondition(Integer.parseInt(getNodeValue("condition", person_nodes)));
        }
        game.setCrewRace(loadRace);
    }



    //copied from http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
    protected Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }
    protected String getNodeValue( Node node ) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++ ) {
            Node data = childNodes.item(x);
            if ( data.getNodeType() == Node.TEXT_NODE )
                return data.getNodeValue();
        }
        return "";
    }
    protected String getNodeValue(String tagName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.TEXT_NODE )
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }
    protected String getNodeAttr(String attrName, Node node ) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++ ) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }
    protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                        if ( data.getNodeName().equalsIgnoreCase(attrName) )
                            return data.getNodeValue();
                    }
                }
            }
        }

        return "";
    }

}
