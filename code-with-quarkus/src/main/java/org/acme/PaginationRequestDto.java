package org.acme;

import io.quarkus.panache.common.Sort.Direction;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public class PaginationRequestDto {
	
	@QueryParam("pageNum")
    @DefaultValue("0")
    private int pageNum;

    @QueryParam("pageSize")
    @DefaultValue("10")
    private int pageSize;
    
    @QueryParam("sort")
    @DefaultValue("id")
    private String sort;
    
    @QueryParam("direction")
    @DefaultValue("Ascending")
    private String dir;

    public PaginationRequestDto(int pageNum, int pageSize, String sort, String dir) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sort = sort;
        this.dir = dir;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }
    
    public String getSort() {
		return sort;
	}
    
    public String getDir() {
		return dir;
	}
    
    public Direction direction(String direction) {    	
    	if(direction.equalsIgnoreCase("Ascending")) {
    		return Direction.Ascending;
    	} else if(direction.equalsIgnoreCase("Descending")) {
    		return Direction.Descending;
    	} else {
    		throw new IllegalArgumentException("direction must be Ascending or Descending");
    	}
    }

}
