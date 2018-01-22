
package generatedJaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Big"/>
 *         &lt;element ref="{}Small"/>
 *       &lt;/sequence>
 *       &lt;attribute name="max-total-rounds" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="fixed" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="additions" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "big",
    "small"
})
@XmlRootElement(name = "Blindes")
public class Blindes {

    @XmlElement(name = "Big", required = true)
    protected BigInteger big;
    @XmlElement(name = "Small", required = true)
    protected BigInteger small;
    @XmlAttribute(name = "max-total-rounds")
    protected BigInteger maxTotalRounds;
    @XmlAttribute(name = "fixed", required = true)
    protected boolean fixed;
    @XmlAttribute(name = "additions")
    protected BigInteger additions;

    /**
     * Gets the value of the big property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBig() {
        return big;
    }

    /**
     * Sets the value of the big property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBig(BigInteger value) {
        this.big = value;
    }

    /**
     * Gets the value of the small property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSmall() {
        return small;
    }

    /**
     * Sets the value of the small property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSmall(BigInteger value) {
        this.small = value;
    }

    /**
     * Gets the value of the maxTotalRounds property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxTotalRounds() {
        return maxTotalRounds;
    }

    /**
     * Sets the value of the maxTotalRounds property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxTotalRounds(BigInteger value) {
        this.maxTotalRounds = value;
    }

    /**
     * Gets the value of the fixed property.
     * 
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Sets the value of the fixed property.
     * 
     */
    public void setFixed(boolean value) {
        this.fixed = value;
    }

    /**
     * Gets the value of the additions property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAdditions() {
        return additions;
    }

    /**
     * Sets the value of the additions property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAdditions(BigInteger value) {
        this.additions = value;
    }

}
