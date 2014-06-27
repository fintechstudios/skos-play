//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.23 at 03:22:09 PM CEST 
//


package fr.sparna.rdf.skos.printer.schema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * One particular way of displaying a thesaurus.
 * 
 * <p>Java class for kosDisplay complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="kosDisplay">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.sparna.fr/thesaurus-display}section" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="displayId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="column-count" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="main" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "kosDisplay", propOrder = {
    "section"
})
public class KosDisplay {

    @XmlElement(required = true)
    protected List<Section> section;
    @XmlAttribute(required = true)
    protected String displayId;
    @XmlAttribute(name = "column-count")
    protected BigInteger columnCount;
    @XmlAttribute
    protected Boolean main;

    /**
     * Gets the value of the section property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Section }
     * 
     * 
     */
    public List<Section> getSection() {
        if (section == null) {
            section = new ArrayList<Section>();
        }
        return this.section;
    }

    /**
     * Gets the value of the displayId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayId() {
        return displayId;
    }

    /**
     * Sets the value of the displayId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayId(String value) {
        this.displayId = value;
    }

    /**
     * Gets the value of the columnCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getColumnCount() {
        return columnCount;
    }

    /**
     * Sets the value of the columnCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setColumnCount(BigInteger value) {
        this.columnCount = value;
    }

    /**
     * Gets the value of the main property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMain() {
        return main;
    }

    /**
     * Sets the value of the main property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMain(Boolean value) {
        this.main = value;
    }

}
