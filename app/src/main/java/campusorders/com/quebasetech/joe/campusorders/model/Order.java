/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 19/ 10/ 2018.
 * MIT License
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package campusorders.com.quebasetech.joe.campusorders.model;

public class Order {
    private String gigId;
    private int qty; //Quantity
    private int total;
    private long orderTime; // Time order is made
    private long deliveryTime; // Time order is fulfilled
    private String location; //Where order is to be delivered
    private String client; // The user who made the order;
    private enum orderStatus { NEW, PENDING, FULFILLED, REJECTED, CANCELLED };
        /* ORDER STATUS
        *  NEW - not seen by the buyer
        *  PENDING - Seller has accepted to deliver
        *  FULFILLED - buyer has received the gig
        *  REJECTED - cancelled by the seller
        *  CANCELLED - cancelled by the buyer
        * */
    private orderStatus status;

    public Order() {
    }

    public Order(String gigId, int qty, int total, long orderTime, long deliveryTime, String location, String client, orderStatus status) {
        this.gigId = gigId;
        this.qty = qty;
        this.total = total;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.location = location;
        this.client = client;
        this.status = status;
    }

    public String getGigId() {
        return gigId;
    }

    public void setGigId(String gigId) {
        this.gigId = gigId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public orderStatus getStatus() {
        return status;
    }

    public void setStatus(orderStatus status) {
        this.status = status;
    }
}
