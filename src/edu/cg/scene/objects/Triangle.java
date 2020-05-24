package edu.cg.scene.objects;

import edu.cg.algebra.*;

//Added functionality for triangle
public class Triangle extends Shape {
    // implicit form of a plain: ax + by + cz + d = 0;
    transient Point p1;
    transient Point p2;
    transient Point p3;
    transient Plain trianglePlain;
    private transient Vec normal = null;
    transient double area;


    // Plain Constructors.
    public Triangle(Point p1,Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.trianglePlain = new Plain(p1.sub(p2).cross(p3.sub(p2)),p1);
        this.area = 0.5 * (p2.sub(p1)).cross(p3.sub(p1)).norm();
    }

    @Override
    public String toString() {
        String endl = System.lineSeparator();
        return "Triangle:" + endl +
                "P1: " + p1 + endl +
                "P2: " + p2 + endl +
                "P3: " + p2 + endl +
                "trianglePlain: " + trianglePlain + endl;
    }

    public synchronized Vec normal() {
        return this.trianglePlain.normal();
    }

    @Override
    public boolean equals(Shape shape){
        return shape instanceof Triangle && ((Triangle) shape).p1.equals(p1) && ((Triangle) shape).p2.equals(p2) && ((Triangle) shape).p3.equals(p3);
        }

    /**
     * Returns the ray intersection with the plain if exists, and null otherwise.
     * Done using trigonometric rules shown in class
     */
    @Override
    public Hit intersect(Ray ray) {
        Hit hit = trianglePlain.intersect(ray);
        Point p = hit.hitPoint;
        if(hit != null){
            double area1 =  0.5 * (p2.sub(p1)).cross(p.sub(p1)).norm();
            double area2 = 0.5 * (p.sub(p1)).cross(p3.sub(p1)).norm();
            double area3 =  0.5 * (p2.sub(p)).cross(p3.sub(p)).norm();
            if(area1 + area2 + area3 != this.area){
                hit = null;
            }
        }
        return hit;
    }
}
