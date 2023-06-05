package kr.co.fns.app.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageUtil {

    private int pageSize = 10; // 한 화면에 보여줄 열의 수 ---
    private int currentPage = 1; // 현재 페이지 ---
    private int startRow; // 시작 열번호 ---
    private int endRow; // 종료 열번호---
    private int count; // 전체 글 개수---
    private int pageCount; // 페이지 갯수 ---
    // 페이지 링크를 달기위한 파라미터
    private int nextParam; // 다음
    private int previousParam; // 이전
    private List<Integer> listParam; // 페이지링크
    private boolean bNextView;
    private boolean bPreviousView;

    public PageUtil() {
        // TODO Auto-generated constructor stub
    }

    public PageUtil(int pageSize, int count, int currentPage, int showPageBlock) {
        super();
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (currentPage == 0) {
            currentPage = 1;
        }
        this.pageSize = pageSize;
        this.count = count;
        this.currentPage = currentPage;
        this.listParam = new ArrayList<Integer>();
        this.bNextView = false;
        this.bPreviousView = false;
        pagingExcute(showPageBlock);
    }

    public PageUtil(int pageSize, int count, int currentPage) {
        super();
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (currentPage == 0) {
            currentPage = 1;
        }
        this.pageSize = pageSize;
        this.count = count;
        this.currentPage = currentPage;
        this.listParam = new ArrayList<Integer>();
        this.bNextView = false;
        this.bPreviousView = false;
        pagingExcute(1);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getNextParam() {
        return nextParam;
    }

    public void setNextParam(int nextParam) {
        this.nextParam = nextParam;
    }

    public int getPreviousParam() {
        return previousParam;
    }

    public void setPreviousParam(int previousParam) {
        this.previousParam = previousParam;
    }

    public List<Integer> getListParam() {
        return listParam;
    }

    public void setListParam(List<Integer> listParam) {
        this.listParam = listParam;
    }

    public boolean isbNextView() {
        return bNextView;
    }

    public void setbNextView(boolean bNextView) {
        this.bNextView = bNextView;
    }

    public boolean isbPreviousView() {
        return bPreviousView;
    }

    public void setbPreviousView(boolean bPreviousView) {
        this.bPreviousView = bPreviousView;
    }

    // 보여줄 블럭의 수,
    public void pagingExcute(int iPageBlock) {
        // currentPage = cPage; // 현재 페이지
        startRow = ((currentPage - 1) * pageSize); //+ 1; // 시작 열번호
        endRow = currentPage * pageSize; // 종료 열번호

        pageCount = 1; // 페이지 갯수
        if (count > 0) {
            pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1); // 총페이지수

            int pageBlock = iPageBlock; // 보여줄 블럭의수
            int startPage = (int) (currentPage / pageBlock) * pageBlock + 1; // 시작 페이지
            int endPage = startPage + pageBlock - 1; // 끝 페이지
            if (endPage > pageCount)
                endPage = pageCount;
            if (startPage > pageBlock) {// [이전] 페이지 - 시작 페이지 - 블럭 크기
                bPreviousView = true;
                previousParam = startPage - pageBlock;
            }
            for (int i = startPage; i <= endPage; i++) { // [1][2][3]...
                listParam.add(i);
            }
            if (endPage < pageCount) { // [다음] 페이지 - 끝페이지 + 블럭 크기
                bNextView = true;
                nextParam = startPage + pageBlock;
            }
        }
    }

    public Map<String, Object> pagingInfo() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageNo", currentPage);
        result.put("pageSize", pageSize);
        result.put("pageCount", pageCount);
        result.put("count", count);
        return result;
    }
}
