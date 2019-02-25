package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2019/1/2.
 */

public class NonRootDir {
    //群组id
    private String id;
    //是否dir
    private String is_dir;
    //名字
    private String name;
    //根id
    private String root_id;
    //path
    private String path;
    //icon
    private String icon;


    public NonRootDir(String id, String is_dir, String name, String root_id, String path, String icon) {
        this.id = id;
        this.is_dir = is_dir;
        this.name = name;
        this.root_id = root_id;
        this.path = path;
        this.icon = icon;
    }

    public NonRootDir() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_dir() {
        return is_dir;
    }

    public void setIs_dir(String is_dir) {
        this.is_dir = is_dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
