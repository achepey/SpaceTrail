package javagame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Daniel on 11/29/2014.
 *
 * XML Structure:
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
 </game>
 */
public class GameFileSaver
{
    Document doc;
    NodeList gameNodes;
    Game game;

    public GameFileSaver(Game g) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            //Creating XML skeleton
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);
            Element shipElement = doc.createElement("ship");
            rootElement.appendChild(shipElement);
            Element resourcesElement = doc.createElement("resources");
            rootElement.appendChild(resourcesElement);
            Element peopleElement = doc.createElement("people");
            rootElement.appendChild(peopleElement);
            Element currPerson;
            for(int i = 0; i < 5; ++i) {
                currPerson = doc.createElement("person" + i);
                peopleElement.appendChild(currPerson);
            }

            //setting gameNodes which will be used in the rest of the saving
            gameNodes = doc.getChildNodes();
            Node gameNode = getNode("game", gameNodes);
            gameNodes = gameNode.getChildNodes();

            //game object
            game = g;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGame(String fileName) {
        try {
            saveShip();
            saveResources();
            savePeople();

            NodeList rootNodeList = doc.getChildNodes();
            Node gameNode = getNode("game", rootNodeList);
            addNode("destinationPlanet", game.getDestination().name, gameNode);
            addNode("distance", Double.toString(game.getDistance()), gameNode);


            //copied from http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveShip() {
        Node ship = getNode("ship", gameNodes);
        addNode("engine", Integer.toString(game.getShip().getEngineStatus()), ship);
        addNode("wing", Integer.toString(game.getShip().getWingStatus()), ship);
        addNode("livingBay", Integer.toString(game.getShip().getLivingBayStatus()), ship);
    }

    private void saveResources() {
        Node resources = getNode("resources", gameNodes);
        addNode("money", Integer.toString(game.getResources().getMoney()), resources);
        addNode("food", Integer.toString(game.getResources().getFood()),resources);
        addNode("fuel", Integer.toString(game.getResources().getFuel()), resources);
        addNode("compound", Integer.toString(game.getResources().getCompound()),resources);
        addNode("aluminum", Integer.toString(game.getResources().getAluminum()), resources);

        //saving spare parts
        ArrayList<Part> partsList = game.getResources().getSpares();
        int numEngines = 0, numWings = 0, numLivingBays = 0;
        for(int i = 0; i < partsList.size(); ++i) {
            if(partsList.get(i).getName().equals("Engine")) {
                ++numEngines;
            }
            else if(partsList.get(i).getName().equals("Wing")) {
                ++numWings;
            }
            else if(partsList.get(i).getName().equals("LivingBay")){
                ++numLivingBays;
            }
        }
        addNode("spareEngines", Integer.toString(numEngines), resources);
        addNode("spareWings", Integer.toString(numWings), resources);
        addNode("spareLivingBays", Integer.toString(numLivingBays), resources);
    }

    private void savePeople() {
        Node people = getNode("people", gameNodes);
        NodeList people_nodes = people.getChildNodes();
        for(int i = 0; i < game.getPeople().size(); ++i) {
            Node person = getNode("person"+i, people_nodes);
            addNode("name", game.getCrew(i).getName(), person);
            addNode("age", Integer.toString(game.getCrew(i).getAge()), person);
            addNode("condition", Integer.toString(game.getCrew(i).getCondition()), person);
            addNode("raceName", game.getCrew(i).getRace().getName(), person);
            addNode("raceStrength", game.getCrew(i).getRace().getStrength(), person);
            addNode("raceWeakness", game.getCrew(i).getRace().getWeakness(), person);
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
