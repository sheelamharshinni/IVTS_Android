package com.tecdatum.Tracking.School.newhelpers;

public class GetVehicleRoutes_List_student {


    String StudentMasterID, StudentName, StudentID, ClassName, ParentName,
    ParentMobile, ParentMobile2, RouteID, PickPointID, DropPointID,PickName,DropName,RouteName,PickVehicleNo,DropVehicleNo;

    public GetVehicleRoutes_List_student(String studentMasterID, String studentName, String studentID, String className, String parentName, String parentMobile, String parentMobile2, String routeID, String pickPointID, String dropPointID, String pickName, String dropName, String routeName, String pickVehicleNo, String dropVehicleNo) {
        StudentMasterID = studentMasterID;
        StudentName = studentName;
        StudentID = studentID;
        ClassName = className;
        ParentName = parentName;
        ParentMobile = parentMobile;
        ParentMobile2 = parentMobile2;
        RouteID = routeID;
        PickPointID = pickPointID;
        DropPointID = dropPointID;
        PickName = pickName;
        DropName = dropName;
        RouteName = routeName;
        PickVehicleNo = pickVehicleNo;
        DropVehicleNo = dropVehicleNo;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public String getPickVehicleNo() {
        return PickVehicleNo;
    }

    public void setPickVehicleNo(String pickVehicleNo) {
        PickVehicleNo = pickVehicleNo;
    }

    public String getDropVehicleNo() {
        return DropVehicleNo;
    }

    public void setDropVehicleNo(String dropVehicleNo) {
        DropVehicleNo = dropVehicleNo;
    }

    public GetVehicleRoutes_List_student(String studentMasterID, String studentName, String studentID, String className, String parentName, String parentMobile, String parentMobile2, String routeID, String pickPointID, String dropPointID, String pickName, String dropName) {
        StudentMasterID = studentMasterID;
        StudentName = studentName;
        StudentID = studentID;
        ClassName = className;
        ParentName = parentName;
        ParentMobile = parentMobile;
        ParentMobile2 = parentMobile2;
        RouteID = routeID;
        PickPointID = pickPointID;
        DropPointID = dropPointID;
        PickName = pickName;
        DropName = dropName;
    }

    public String getPickName() {
        return PickName;
    }

    public void setPickName(String pickName) {
        PickName = pickName;
    }

    public String getDropName() {
        return DropName;
    }

    public void setDropName(String dropName) {
        DropName = dropName;
    }

    public String getStudentMasterID() {
        return StudentMasterID;
    }

    public void setStudentMasterID(String studentMasterID) {
        StudentMasterID = studentMasterID;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getParentMobile() {
        return ParentMobile;
    }

    public void setParentMobile(String parentMobile) {
        ParentMobile = parentMobile;
    }

    public String getParentMobile2() {
        return ParentMobile2;
    }

    public void setParentMobile2(String parentMobile2) {
        ParentMobile2 = parentMobile2;
    }

    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    public String getPickPointID() {
        return PickPointID;
    }

    public void setPickPointID(String pickPointID) {
        PickPointID = pickPointID;
    }

    public String getDropPointID() {
        return DropPointID;
    }

    public void setDropPointID(String dropPointID) {
        DropPointID = dropPointID;
    }

}

