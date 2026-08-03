package com.example.demo.domain.ranking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.ranking.dto.response.GetProductRankingResponse;
import com.example.demo.domain.ranking.service.RankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/ranking")
public class RankingController {

	private final RankingService rankingService;

	@GetMapping
	public ResponseEntity<List<GetProductRankingResponse>> getProductRanking(@RequestParam(defaultValue = "10") int count ){
		return ResponseEntity.status(HttpStatus.OK).body(rankingService.findProductTopNInToday(count));
	}
}
