package com.idreameducation.ipreppal.model;

import java.io.Serializable;

/**
 * Created by nice on 30-Oct-17.
 */

public class ComulativeMasteryModel implements Serializable {

   public String getComulativeMastery() {
      return cumulativeMastery;
   }

   public void setComulativeMastery(String comulativeMastery) {
      this.cumulativeMastery = comulativeMastery;
   }

   private String cumulativeMastery;

   public String getLastAttamptedDate() {
      return lastAttamptedDate;
   }

   public void setLastAttamptedDate(String lastAttamptedDate) {
      this.lastAttamptedDate = lastAttamptedDate;
   }

   private String lastAttamptedDate;







}
