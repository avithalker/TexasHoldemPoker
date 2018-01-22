
package generatedJaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}HandsCount"/>
 *         &lt;element ref="{}Buy"/>
 *         &lt;element ref="{}Blindes"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "handsCount",
    "buy",
    "blindes"
})
@XmlRootElement(name = "Structure")
public class Structure {

    @XmlElement(name = "HandsCount", required = true)
    protected BigInteger handsCount;
    @XmlElement(name = "Buy", required = true)
    protected BigInteger buy;
    @XmlElement(name = "Blindes", required = true)
    protected Blindes blindes;

    /**
     * Gets the value of the handsCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHandsCount() {
        return handsCount;
    }

    /**
     * Sets the value of the handsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHandsCount(BigInteger value) {
        this.handsCount = value;
    }

    /**
     * Gets the value of the buy property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBuy() {
        return buy;
    }

    /**
     * Sets the value of the buy property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBuy(BigInteger value) {
        this.buy = value;
    }

    /**
     * Gets the value of the blindes property.
     * 
     * @return
     *     possible object is
     *     {@link Blindes }
     *     
     */
    public Blindes getBlindes() {
        return blindes;
    }

    /**
     * Sets the value of the blindes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Blindes }
     *     
     */
    public void setBlindes(Blindes value) {
        this.blindes = value;
    }

}
