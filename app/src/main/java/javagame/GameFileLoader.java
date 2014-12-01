package javagame;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Daniel on 11/28/2014.
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
 </game>

 */
public class GameFileLoader
{
    Document doc;
    NodeList game_nodes;
    Game game;

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

    public Game loadGame() {
        loadShip();
        loadResources();
        loadPeople();
        String destinationPlanet = getNodeValue("destinationPlanet", game_nodes);
        game.setDestination(destinationPlanet);
        game.setDistance(Integer.parseInt(getNodeValue("distance", game_nodes)));
        game.setMoney(Integer.parseInt(getNodeValue("money", game_nodes)));
        return game;
    }

    private void loadShip() {
        Node ship = getNode("ship", game_nodes);
        NodeList ship_child_nodes = ship.getChildNodes();
        game.getShip().setHullStatus(Integer.parseInt(getNodeValue("hull", ship_child_nodes)));
        game.getShip().setEngineStatus(Integer.parseInt(getNodeValue("engine",ship_child_nodes)));
        game.getShip().setWingStatus(Integer.parseInt(getNodeValue("wing",ship_child_nodes)));
        game.getShip().setLivingBayStatus(Integer.parseInt((getNodeValue("livingBay",ship_child_nodes))));
    }
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
            game.getResources().addSpare(new Part("Engine", 100));
        }
        for(int i = 0; i < spareWings; ++i) {
            game.getResources().addSpare(new Part("Wing", 100));
        }
        for(int i = 0; i < spareLivingBays; ++i) {
            game.getResources().addSpare(new Part("LivingBay", 100));
        }
    }
    private void loadPeople() {
        Node people = getNode("people", game_nodes);
        NodeList people_child_nodes = people.getChildNodes();
        //People
        for(int i = 0; i < people_child_nodes.getLength(); ++i) {
            Node person = getNode("person"+i, people_child_nodes);
            NodeList person_nodes = person.getChildNodes();
            if(i == 0) {
                game.addCrew(getNodeValue("name",person_nodes), true);
            }
            else {
                game.addCrew(getNodeValue("name",person_nodes), false);
            }
            game.getCrew(i).setAge(Integer.parseInt(getNodeValue("age", person_nodes)));
            game.getCrew(i).setCondition(Integer.parseInt(getNodeValue("condition", person_nodes)));
            Race loadRace = new Race();
            loadRace.setName(getNodeValue("raceName", person_nodes));
            loadRace.setStrength(getNodeValue("raceStrength", person_nodes));
            loadRace.setWeakness(getNodeValue("raceWeakness", person_nodes));
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

}
