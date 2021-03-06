//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.26 at 10:36:31 AM CET 
//


package fr.sparna.rdf.skos.printer.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *     			A section in the output document, typically to organize
 *     			output alphabetically. @title : title of the section.
 *     		
 * 
 * <p>Java class for section complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="section">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.sparna.fr/thesaurus-display}list"/>
 *         &lt;element ref="{http://www.sparna.fr/thesaurus-display}tree"/>
 *         &lt;element ref="{http://www.sparna.fr/thesaurus-display}table"/>
 *         &lt;element ref="{http://www.sparna.fr/thesaurus-display}index"/>
 *       &lt;/choice>
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "section", propOrder = {
    "list",
    "tree",
    "table",
    "index"
})
public class Section {

    protected List list;
    protected Tree tree;
    protected Table table;
    protected Index index;
    @XmlAttribute(required = true)
    protected String title;

    /**
     * Gets the value of the list property.
     * 
     * @return
     *     possible object is
     *     {@link List }
     *     
     */
    public List getList() {
        return list;
    }

    /**
     * Sets the value of the list property.
     * 
     * @param value
     *     allowed object is
     *     {@link List }
     *     
     */
    public void setList(List value) {
        this.list = value;
    }

    /**
     * Gets the value of the tree property.
     * 
     * @return
     *     possible object is
     *     {@link Tree }
     *     
     */
    public Tree getTree() {
        return tree;
    }

    /**
     * Sets the value of the tree property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tree }
     *     
     */
    public void setTree(Tree value) {
        this.tree = value;
    }

    /**
     * Gets the value of the table property.
     * 
     * @return
     *     possible object is
     *     {@link Table }
     *     
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sets the value of the table property.
     * 
     * @param value
     *     allowed object is
     *     {@link Table }
     *     
     */
    public void setTable(Table value) {
        this.table = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link Index }
     *     
     */
    public Index getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link Index }
     *     
     */
    public void setIndex(Index value) {
        this.index = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

}
