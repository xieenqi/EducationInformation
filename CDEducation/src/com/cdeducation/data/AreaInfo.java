package com.cdeducation.data;


import java.io.Serializable;

/**
 * @author t77yq on 14-2-20.
 */
public class AreaInfo implements Serializable
{

  private static final long serialVersionUID = 3251054922741760103L;

  public int id;

  public String name;

  public int parentId;

  public int capitalId;

  public String extra;
}
