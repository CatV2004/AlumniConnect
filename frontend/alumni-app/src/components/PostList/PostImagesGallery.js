import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";

const PostImagesGallery = ({ images }) => {
  if (!images || images.length === 0) return null;
  if (images.length === 1) {
    return (
      <div className="w-full">
        <img
          src={images[0].image}
          alt="post"
          className="w-full h-auto max-h-[500px] object-contain bg-black"
        />
      </div>
    );
  }

  return (
    <Swiper
      modules={[Navigation, Pagination, Autoplay]}
      spaceBetween={0}
      slidesPerView={1}
      navigation
      pagination={{ clickable: true }}
      autoplay={{ delay: 5000 }}
      className="w-full h-auto max-h-[500px] bg-black"
    >
      {images.map((imgObj) => (
        <SwiperSlide key={imgObj.id}>
          <img
            src={imgObj.image}
            alt={`post-${imgObj.id}`}
            className="w-full h-auto max-h-[500px] object-contain"
          />
        </SwiperSlide>
      ))}
    </Swiper>
  );
};

export default PostImagesGallery;
