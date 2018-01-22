
package generatedJaxb;

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
 *         &lt;element ref="{}GameType"/>
 *         &lt;element ref="{}Structure"/>
 *         &lt;element ref="{}Players" minOccurs="0"/>
 *         &lt;element ref="{}DynamicPlayers" minOccurs="0"/>
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
    "gameType",
    "structure",
    "players",
    "dynamicPlayers"
})
@XmlRootElement(name = "GameDescriptor")
public class GameDescriptor {

    @XmlElement(name = "GameType", required = true)
    protected String gameType;
    @XmlElement(name = "Structure", required = true)
    protected Structure structure;
    @XmlElement(name = "Players")
    protected Players players;
    @XmlElement(name = "DynamicPlayers")
    protected DynamicPlayers dynamicPlayers;

    /**
     * Gets the value of the gameType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * Sets the value of the gameType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGameType(String value) {
        this.gameType = value;
    }

    /**
     * Gets the value of the structure property.
     * 
     * @return
     *     possible object is
     *     {@link Structure }
     *     
     */
    public Structure getStructure() {
        return structure;
    }

    /**
     * Sets the value of the structure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Structure }
     *     
     */
    public void setStructure(Structure value) {
        this.structure = value;
    }

    /**
     * Gets the value of the players property.
     * 
     * @return
     *     possible object is
     *     {@link Players }
     *     
     */
    public Players getPlayers() {
        return players;
    }

    /**
     * Sets the value of the players property.
     * 
     * @param value
     *     allowed object is
     *     {@link Players }
     *     
     */
    public void setPlayers(Players value) {
        this.players = value;
    }

    /**
     * Gets the value of the dynamicPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicPlayers }
     *     
     */
    public DynamicPlayers getDynamicPlayers() {
        return dynamicPlayers;
    }

    /**
     * Sets the value of the dynamicPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicPlayers }
     *     
     */
    public void setDynamicPlayers(DynamicPlayers value) {
        this.dynamicPlayers = value;
    }

}
