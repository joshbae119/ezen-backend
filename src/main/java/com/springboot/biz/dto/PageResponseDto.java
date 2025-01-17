package com.springboot.biz.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.Data;
@Data
public class PageResponseDto<E> {
    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDto pageRequestDTO;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;
    @Builder(builderMethodName = "withAll")
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) totalCount;
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0) * 10);
        int start = end - 9;
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));
        end = end > last ? last : end;
        this.prev = start > 1;
        this.next = totalCount > end * pageRequestDTO.getSize();
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        if (prev) {
            prevPage = start - 1;
        }
        if (next) {
            nextPage = end + 1;
        }
        this.totalPage = this.pageNumList.size();
        this.current = pageRequestDTO.getPage();
    }
}