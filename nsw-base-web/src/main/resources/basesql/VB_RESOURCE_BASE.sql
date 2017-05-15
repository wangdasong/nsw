CREATE OR REPLACE VIEW VB_RESOURCE_BASE AS
SELECT MP.ID   MP_ID,
       MP.PARENT_ID MP_PARENT_ID,
       MP.CODE MP_CODE,
       MP.NAME MP_NAME,
       MP.SORT_NO MP_SORT,
       C.ID    C_ID,
       C.CODE  C_CODE,
       C.NAME  C_NAME,
       C.SORT  C_SORT,
       W.ID    W_ID,
       W.CODE  W_CODE,
       W.NAME  W_NAME,
       W.SORT  W_SORT,
       PC.ID   PC_ID,
       PC.CODE PC_CODE,
       PC.NAME PC_NAME,
       PC.SORT PC_SORT,
       PW.ID   PW_ID,
       PW.CODE PW_CODE,
       PW.NAME PW_NAME,
       PW.SORT PW_SORT
  FROM (SELECT E.ID,E.PARENT_ID, E.CODE, E.NAME, SUBSTR(A.ATT_VALUE, 10, 32) PAGE_ID, NVL(AO.ATT_VALUE,1) SORT_NO
          FROM SMS_ELEMENT E, SMS_ATT_CONFIG A, SMS_ATT_CONFIG AO
         WHERE E.ID = A.BELONG_ID(+)
           AND A.TYPE(+) = 'url'
           AND E.ID = AO.BELONG_ID(+)
           AND AO.TYPE(+) = 'order'
           AND E.WIDGET_ID =
               (SELECT W.ID
                  FROM SMS_WIDGET W
                 WHERE W.CONTAINER_ID = 'systemmenu')) MP,
       SMS_CONTAINER C,
       (SELECT W.ID,
               W.CODE,
               W.NAME,
               W.CONTAINER_ID,
               W.SORT,
               SUBSTR(A.ATT_VALUE, 16, 32) POP_PAGE_ID
          FROM SMS_WIDGET W, SMS_ATT_CONFIG A
         WHERE W.ID = A.BELONG_ID(+)
           AND A.TYPE(+) = 'output'
           AND SUBSTR(A.ATT_VALUE(+), 1, 5) = 'popup') W,
       SMS_CONTAINER PC,
       SMS_WIDGET PW
 WHERE MP.PAGE_ID = C.PAGE_ID(+)
   AND C.ID = W.CONTAINER_ID(+)
   AND W.POP_PAGE_ID = PC.PAGE_ID(+)
   AND PC.ID = PW.CONTAINER_ID(+);
