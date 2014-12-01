package javagame;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Daniel on 11/30/2014.
 */
public class Planet
{
    private String fileName;

    public String name, compound1, compound2;
    public double mass, diameter, density, gravity, escapeVelocity, rotationPeriod, lengthOfDay, distanceFromSun, orbitalPeriod, orbitalVelocity, orbitalInclination, axialTilt, meanTemperature, surfacePressure, fuelCost, foodCost, partCost, aluminumCost;
    public int numberOfMoons;
    public Boolean ringSystem, globalMagneticField, visited;

    public Planet(String n) {
        try {
            fileName = ".\\app\\src\\main\\java\\javagame\\planetData.xml";
            name = n;
            visited = false;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileName);
            NodeList planets = doc.getChildNodes();
            Node planetNode = getNode("resources", planets);
            planets = planetNode.getChildNodes();
            //planets should now contain list of planets in solar system
            Node planet = getNode(name, planets);
            //planet will be planet from XML document
            NodeList planetInfo = planet.getChildNodes();
            //planetInfo contains list of all planet attributes
            //Begin setting instance fields
            compound1 = getNodeValue("compound1", planetInfo);
            compound2 = getNodeValue("compound2", planetInfo);
            mass = Double.parseDouble(getNodeValue("mass", planetInfo));
            diameter = Double.parseDouble(getNodeValue("diameter", planetInfo));
            density = Double.parseDouble(getNodeValue("density", planetInfo));
            gravity = Double.parseDouble(getNodeValue("gravity", planetInfo));
            escapeVelocity = Double.parseDouble(getNodeValue("escapeVelocity", planetInfo));
            rotationPeriod = Double.parseDouble(getNodeValue("rotationPeriod", planetInfo));
            lengthOfDay = Double.parseDouble(getNodeValue("lengthOfDay", planetInfo));
            distanceFromSun = Double.parseDouble(getNodeValue("distanceFromSun", planetInfo));
            orbitalPeriod = Double.parseDouble(getNodeValue("orbitalPeriod", planetInfo));
            orbitalVelocity = Double.parseDouble(getNodeValue("orbitalVelocity", planetInfo));
            orbitalInclination = Double.parseDouble(getNodeValue("orbitalInclination", planetInfo));
            axialTilt = Double.parseDouble(getNodeValue("axialTilt", planetInfo));
            meanTemperature = Double.parseDouble(getNodeValue("meanTemperature", planetInfo));
            surfacePressure = Double.parseDouble(getNodeValue("surfacePressure", planetInfo));
            numberOfMoons = Integer.parseInt(getNodeValue("numberOfMoons", planetInfo));
            if(getNodeValue("ringSystem", planetInfo).equals("Yes")) {
                ringSystem = true;
            }
            else {
                ringSystem = false;
            }
            if(getNodeValue("globalMagneticField", planetInfo).equals("Yes")) {
                globalMagneticField = true;
            }
            else {
                globalMagneticField = false;
            }

            if(distanceFromSun > 2200) {
                fuelCost = 10;
                foodCost = 5;
            }else if(distanceFromSun > 600) {
                fuelCost = 10;
                foodCost = 1;
            }else {
                fuelCost = 5;
                foodCost = 5;
            }
            if(name.equals("Earth") || name.equals("Pluto") || name.equals("Mars") || name.equals("Neptune") ) {
                aluminumCost = 10;
                partCost = 15;
            }else{
                aluminumCost = 15;
                partCost = 25;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean equals(Planet p){
        return name.equals(p.name);
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
