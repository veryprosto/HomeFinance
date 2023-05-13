package ru.veryprosto.homefinance.model;

public class DTO {

    private String leftUp;
    private String rightUp;
    private String leftDown;
    private String rightDown;
    private int iconResource;
    private OperationType operationType;
    private String parentClassName;
    private Integer id;

    public DTO() {
    }

    public DTO(String leftUp, String rightUp, String leftDown, String rightDown, int iconResource, OperationType operationType) {
        this.leftUp = leftUp;
        this.rightUp = rightUp;
        this.leftDown = leftDown;
        this.rightDown = rightDown;
        this.iconResource = iconResource;
        this.operationType = operationType;
    }

    public DTO(String leftUp) {
        this.leftUp = leftUp;
    }

    public String getLeftUp() {
        return leftUp;
    }

    public void setLeftUp(String leftUp) {
        this.leftUp = leftUp;
    }

    public String getRightUp() {
        return rightUp;
    }

    public void setRightUp(String rightUp) {
        this.rightUp = rightUp;
    }

    public String getLeftDown() {
        return leftDown;
    }

    public void setLeftDown(String leftDown) {
        this.leftDown = leftDown;
    }

    public String getRightDown() {
        return rightDown;
    }

    public void setRightDown(String rightDown) {
        this.rightDown = rightDown;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentClassName() {
        return parentClassName;
    }

    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
    }
}

