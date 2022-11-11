package animals.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    private String data;
    private Node yes;
    private Node no;

    public Node() {}

    public Node(final String data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return no == null && yes == null;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public Node getYes() {
        return yes;
    }

    public void setYes(Node yes) {
        this.yes = yes;
    }

    public Node getNo() {
        return no;
    }

    public void setNo(Node no) {
        this.no = no;
    }

    @JsonIgnore
    public int getNumberOfNode() {
        if (this.isLeaf()) {
            return 1;
        }
        return 1 + yes.getNumberOfNode() + no.getNumberOfNode();
    }

    @JsonIgnore
    public int getNumberOfLeaf() {
        if (this.isLeaf()) {
            return 1;
        }
        return yes.getNumberOfLeaf() + no.getNumberOfLeaf();
    }


}
