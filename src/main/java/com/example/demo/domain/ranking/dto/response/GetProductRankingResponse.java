package com.example.demo.domain.ranking.dto.response;

public record GetProductRankingResponse(
	Long id,
	String title,
	String thumbnailUrl,
	double score
) {
}
