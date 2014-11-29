package javagame;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Daniel on 11/28/2014.
 XML Structure:
 <Game>
    <Ship>
        ...ship stuff
    </Ship>
    <Resources>
        ...resources stuff
    </Resources>
    <People>
        <Person1>
            ...person stuff
        </Person1>
        <Person2>

        </Person2>
        ...to Person5
    </People>
    <DestinationPlanet></DestinationPlanet>
    <Distance></Distance>
 </Game>

 */
public class GameFile
{
    Document doc;
    NodeList game_node;
    Game game;

    public GameFile(String fileName) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(fileName);
            game_node = doc.getChildNodes();

            game = new Game();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Game loadGame() {
        loadShip();
        loadResources();
        loadPeople();
        String destinationPlanet = getNodeValue("DestinationPlanet", game_node);
        game.setDestination(destinationPlanet);
        game.setDistance(Integer.parseInt(getNodeValue("Distance", game_node)));
        return game;
    }

    public void loadShip() {
        Node ship = getNode("Ship",game_node);
        NodeList ship_child_nodes = ship.getChildNodes();
        game.getShip().setEngineStatus(Integer.parseInt(getNodeValue("Engine",ship_child_nodes)));
        game.getShip().setWingStatus(Integer.parseInt(getNodeValue("Wing",ship_child_nodes)));
        game.getShip().setLivingBayStatus(Integer.parseInt((getNodeValue("LivingBay",ship_child_nodes))));
    }
    public void loadResources() {
        Node resources = getNode("Resources", game_node);
        NodeList resources_child_nodes = resources.getChildNodes();
        //Resources
        game.getResources().setMoney(Integer.parseInt(getNodeValue("Money", resources_child_nodes)));
        game.getResources().setFood(Integer.parseInt(getNodeValue("Food", resources_child_nodes)));
        game.getResources().setFuel(Integer.parseInt(getNodeValue("Fuel", resources_child_nodes)));
        game.getResources().setCompound(Integer.parseInt(getNodeValue("Compound", resources_child_nodes)));
        game.getResources().setAluminum(Integer.parseInt(getNodeValue("Aluminum", resources_child_nodes)));

        //Spare parts
        int spareEngines, spareWings, spareLivingBays;
        spareEngines = Integer.parseInt(getNodeValue("Engines",resources_child_nodes));
        spareWings = Integer.parseInt(getNodeValue("Wings", resources_child_nodes));
        spareLivingBays = Integer.parseInt(getNodeValue("LivingBays", resources_child_nodes));
        for(int i = 0; i < spareEngines; ++i) {
            game.getResources().addSpare(new Part("Engine", 100));
        }
        for(int i = 0; i < spareWings; ++i) {
            game.getResources().addSpare(new Part("Wings", 100));
        }
        for(int i = 0; i < spareLivingBays; ++i) {
            game.getResources().addSpare(new Part("LivingBays", 100));
        }
    }
    public void loadPeople() {
        Node people = getNode("People", game_node);
        NodeList people_child_nodes = people.getChildNodes();
        //People
        for(int i = 0; i < people_child_nodes.getLength(); ++i) {
            Node person = getNode("Person"+i, people_child_nodes);
            NodeList person_nodes = person.getChildNodes();
            if(i == 0) {
                game.addCrew(getNodeValue("Name",person_nodes), true);
            }
            else {
                game.addCrew(getNodeValue("Name",person_nodes), false);
            }
            game.getCrew(i).setGender("Male".equals(getNodeValue("Gender", person_nodes)));
            game.getCrew(i).setAge(Integer.parseInt(getNodeValue("Age", person_nodes)));
            game.getCrew(i).setCondition(Integer.parseInt(getNodeValue("Condition", person_nodes)));
            Race loadRace = new Race();
            loadRace.setName(getNodeValue("RaceName", person_nodes));
            loadRace.setStrength(getNodeValue("RaceStrength", person_nodes));
            loadRace.setWeakness(getNodeValue("RaceWeakness", person_nodes));
            game.getCrew(i).setRace(loadRace);
        }
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
    //copied from http://www.drdobbs.com/jvm/creating-and-modifying-xml-in-java/240150782
    protected void setNodeValue(String tagName, String value, NodeList nodes) {
        Node node = getNode(tagName, nodes);
        if ( node == null )
            return;

        // Locate the child text node and change its value
        NodeList childNodes = node.getChildNodes();
        for (int y = 0; y < childNodes.getLength(); y++ ) {
            Node data = childNodes.item(y);
            if ( data.getNodeType() == Node.TEXT_NODE ) {
                data.setNodeValue(value);
                return;
            }
        }
    }
    protected void addNode(String tagName, String value, Node parent) {
        Document dom = parent.getOwnerDocument();

        // Create a new Node with the given tag name
        Node node = dom.createElement(tagName);

        // Add the node value as a child text node
        Text nodeVal = dom.createTextNode(value);
        Node c = node.appendChild(nodeVal);

        // Add the new node structure to the parent node
        parent.appendChild(node);
    }
}
