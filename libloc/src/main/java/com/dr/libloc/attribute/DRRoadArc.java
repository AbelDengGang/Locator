package com.dr.libloc.attribute;


public class DRRoadArc {
    String vertext0Name;
    String vertext1Name;
    DRRoadVertex vetext0;

    public DRRoadVertex getVetext0() {
        return vetext0;
    }

    public void setVetext0(DRRoadVertex vetext0) {
        this.vetext0 = vetext0;
    }

    public DRRoadVertex getVetext1() {
        return vetext1;
    }

    public void setVetext1(DRRoadVertex vetext1) {
        this.vetext1 = vetext1;
    }

    public float getK() {
        return K;
    }

    public void setK(float k) {
        K = k;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    DRRoadVertex vetext1;
    float width = 1.0f;
    float K,b;   // 直线方程参数 y=Kx+b
    String name;

    // 根据端点计算直线方程参数
    public void calLinearParam(){
        if(vetext1.getX() == vetext0.getX()){
            // 垂直于X轴
            K = Float.POSITIVE_INFINITY;
            b =  Float.NaN;
        }else if (vetext1.getY() == vetext0.getY()){
            // 垂直于Y轴
            K = 0;
            b = vetext1.getY();
        }
        else{
            K = (vetext1.getY() - vetext0.getY()) / (vetext1.getX() - vetext0.getX());
            b = vetext0.getY() - K * vetext0.getX();
        }
    }

    // return the distance from point to arc
    // 直线方程 y=Kx+b-> y-Kx -b = 0
    public float calDistence2D(DR3DPoint p){
        // d=abs(x - Ky - b) / sqrt(1+K^2)
        float d;
        if (K == Float.POSITIVE_INFINITY){
            // 垂直于X轴
            d = Math.abs(p.x - vetext0.getX());
        }else if (K == 0){
            // 垂直于Y轴
            d = Math.abs(p.y - b);
        }else {
            d = (float) (Math.abs(p.x - K * p.y - b) / Math.sqrt(1 + K * K));
        }
        return d;
    }

    // 返回点在线段上的投影
    public DR3DPoint porject2D(DR3DPoint p){
        DR3DPoint M = new DR3DPoint();
        M.z = p.z; // 在同一个平面上
        if (K == Float.POSITIVE_INFINITY){
            // 垂直于X轴
            M.x = vetext0.getX();
            M.y = p.y;
        }else if (K == 0) {
            // 垂直于Y轴
            M.x = p.x;
            M.y = b;
        }else {
            M.x = (K * (p.y - b) + p.x) / (K * K + 1);
            M.y = K * M.x + b;
        }
        return M;
    }

    public boolean isPointInArc(DR3DPoint p){
        float err = 0.001f;
        float v1_x = p.x - vetext0.getX();
        float v1_y = p.y - vetext0.getY();

        float v2_x = p.x - vetext1.getX();
        float v2_y = p.y - vetext1.getY();

        // o-------------->o<--------o
        // vetext0         p   vetext1
        // ---v1---------->p<----v2---
        // v1, v2 同向， 说明 p在线段的两边，
        // dot(v1,v2) > 0
        float dot = v1_x * v2_x + v1_y * v2_y;
        if (dot < err){
            return true;
        }else{
            return false;
        }
    }


    public String getVertext0Name() {
        return vertext0Name;
    }

    public void setVertext0Name(String vertext0) {
        this.vertext0Name = vertext0;
    }

    public String getVertext1Name() {
        return vertext1Name;
    }

    public void setVertext1Name(String vertext1) {
        this.vertext1Name = vertext1;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
