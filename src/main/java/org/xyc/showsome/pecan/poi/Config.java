package org.xyc.showsome.pecan.poi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Config extends HttpServlet{
    private static final long serialVersionUID = 1L;
    //Config c = new Config();//此处决不能 new 本类，否则报错：java.lang.StackOverflowError
    String class_path = this.getClass().getResource("").getPath();
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        this.doGet(request,response); //将 表单  post 方法传过来的参数，转给 get方法 去处理
    }
    public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8"); //转码
        String forms=(String)request.getParameter("forms");

        if(forms.equals("if_3g")){
            try {
                this.read_Excel(request,response);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("index.jsp?done=true");
    }


    @SuppressWarnings("rawtypes")
    public void read_Excel(HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException, SQLException, ClassNotFoundException  {

        PropertyConfigurator.configure(class_path+"log4j.properties");//获取 log4j 配置文件
        Logger logger = Logger.getLogger(Config.class ); //获取log4j的实例
        String startTmie = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logger.debug("\n**********【准备处理，正在加载文件】**********");//7
        logger.debug("\n\n**********每行数据间请不要有空行，以免程序误判**********");//7
        logger.debug("\n\n**********由于处理Excel的插件本身性能局限，请控制文件大小，以免影响加载效率。**********");//7
        logger.debug("\n\n**********大小在\t 7 MB \t以内，大约\t 21 万 \t行数据。**********");//7
        logger.debug("\n\n**********本程序每分钟约处理 7000条数据，但根据文件大小会有影响。**********");//7

        request.setCharacterEncoding("UTF-8");  //转码
        String xls_read_Address=(String)request.getParameter("xls_read_Address");//读取
        String xls_write_Address=(String)request.getParameter("xls_write_Address");//写入
        String  count_rows=(String)request.getParameter("count_rows");//自动编号
        String  tips_cmd=(String)request.getParameter("tips_cmd");//CMD窗口的提示方式

        try {
//            DataConvert dc = new DataConvert();//数据转换工具
            DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();

            ArrayList<ArrayList> ls = new  ArrayList<ArrayList>();

            File excel_file = new File(xls_read_Address);//读取的文件路径
            FileInputStream input = new FileInputStream(excel_file);  //读取的文件路径
            XSSFWorkbook wb = new XSSFWorkbook(new BufferedInputStream(input));
            int sheet_numbers = wb.getNumberOfSheets();//获取表的总数
            logger.debug("\n\n**********共有工作表总数**********："+sheet_numbers);//7
            String[] sheetnames=new String[sheet_numbers];

            Connection con=null;
            Statement  stmt=null;
            ResultSet  rs=null;
            String s_3g=null;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@12.12.12.123:1521:gz10000","gz12345","12345");
            stmt = con.createStatement();

            for(int i=0;i<sheet_numbers;i++){//遍历所有表
                ArrayList<String[]> ls_a = new  ArrayList<String[]>(); //用来存储某个表 读取出来的数据
                Sheet sheet = wb.getSheetAt(i);  //获取 某个表
                sheetnames[i] = sheet.getSheetName();//获取表名，存入数组
                logger.debug("\n\n---正在读取和匹配工作表\t《"+sheetnames[i]+"》\t的数据---\n");//7

                int rows_num = sheet.getLastRowNum();//获取行数
                logger.debug("\n\n---表\t《"+sheetnames[i]+"》\t 共有数据---：\t"+rows_num+"\t行");//7
                for( int rows=0;rows<rows_num;rows++){
                    Row row = sheet.getRow(rows);//取得某一行   对象

                    if(row!=null&&!(row.equals(""))){
                        int columns_num = row.getLastCellNum();//获取列数

                        String[] s =new String[5];//初始化数组长度
                        for( int columns=0;columns<columns_num;columns++){
                            Cell  cell = row.getCell(columns);
                            if(cell!=null){
                                switch ( cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING: // 字符串
                                        s[columns] = cell.getStringCellValue();
                                        if(s[columns]==null){
                                            s[columns]=" ";
                                        }
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字
                                        double strCell = cell.getNumericCellValue();
                                        if(String.valueOf(strCell)==null){
                                            s[columns]=" ";
                                        }

                                        df.applyPattern("0");
                                        s[columns] = df.format(strCell);
                                        if(Double.parseDouble(s[columns])!=strCell){
                                            df.applyPattern(Double.toString(strCell));
                                            s[columns] = df.format(strCell);
                                        }

                                        break;
                                    case XSSFCell.CELL_TYPE_BLANK: // 空值
                                        s[columns]=" ";
                                        break;
                                    default:
                                        logger.debug("\n---单元格格式不支持---");
                                        break;
                                }
                            }
                        }
                        if(count_rows.equals("是")&&rows>0){
//                            s[0]=dc.intToString(rows);//自动编号
//                            s[0]=dc.intToString(rows);//自动编号
                        }

                    /* ******** 访问数据库 ，并判断是否3G ******** */
                        String sql="select busiattr1 from ap_t_si_cus_spec_info where cus_phone='"+s[1]+"' and rownum=1";
                        rs = stmt.executeQuery(sql);

                        if(rs.next()){
                            if(rs.getString("busiattr1")!=null){
                                s_3g = rs.getString("busiattr1").toString().toUpperCase();
                            }
                            else{
                                s_3g=" ";
                            }
                        }
                        else{
                            s_3g=" ";
                        }
                    /* ******** 访问结束  ******** */

                        if(s_3g.contains("3G")){
                            s[4]="是";//写入 “是否3G”这一列 的值，比如 “是”
                        }
                        if(s[4]==null){
                            s[4]="\t";
                        }

                    /* CMD窗口提示方式 */
                        if(!(tips_cmd.equals("none"))&&tips_cmd!=null&&!(tips_cmd.equals(""))) {
                            if(tips_cmd.equals("all")){
                                logger.debug("\n匹配中："+s[0]+"\t"+s[1]+"\t"+s[2]+"\t"+s[3]+"\t"+ s[4]);
                            }else {

                            }
//                            if(rows%DataConvert.stringToInt(tips_cmd)==0){
//                                logger.debug("\n匹配中："+s[0]+"\t"+s[1]+"\t"+s[2]+"\t"+s[3]+"\t"+ s[4]);
//                            }
                        }

                        ls_a.add(s);//添加每行数据到 ls_a
                    }
                }
                ls.add(ls_a);       //添加 每个表 到 ls
                input.close();
                write_Excel( xls_write_Address, ls, sheetnames ,tips_cmd)  ;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String endTmie = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logger.debug("\n\n***************【处理完成，程序结束】***************");//7
        logger.debug("\n开始时间："+startTmie);
        logger.debug("\n结束时间："+endTmie);
        logger.debug("\n新文件输出路径为："+xls_write_Address);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void write_Excel( String xls_write_Address,ArrayList<ArrayList> ls,String[] sheetnames,String tips_cmd ) throws IOException  {

        PropertyConfigurator.configure(class_path+"log4j.properties");//获取 log4j 配置文件
        Logger logger = Logger.getLogger(Config.class ); //获取log4j的实例
        FileOutputStream output = new FileOutputStream(new File(xls_write_Address));  //读取的文件路径
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘


        for(int sn=0;sn<ls.size();sn++){
            Sheet sheet = wb.createSheet(String.valueOf(sn));

            wb.setSheetName(sn, sheetnames[sn]);
            ArrayList<String[]> ls2 = ls.get(sn);
            for(int i=0;i<ls2.size();i++){
                Row row = sheet.createRow(i);
                String[] s = ls2.get(i);
                for(int cols=0;cols<s.length;cols++){
                    Cell cell = row.createCell(cols);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式
                    sheet.setColumnWidth(cols, s[cols].length()*384); //设置单元格宽度
                    cell.setCellValue(s[cols]);//写入内容
                }
                /* CMD窗口提示方式 */
                if(!(tips_cmd.equals("none"))&&tips_cmd!=null&&!(tips_cmd.equals(""))) {
                    if(tips_cmd.equals("all")){
                        logger.debug("\n写入中："+s[0]+"\t"+s[1]+"\t"+s[2]+"\t"+s[3]+"\t"+ s[4]);
                    }else {

                    }
//                    if(i%DataConvert.stringToInt(tips_cmd)==0){
//                        logger.debug("\n写入中："+s[0]+"\t"+s[1]+"\t"+s[2]+"\t"+s[3]+"\t"+ s[4]);
//                    }
                }
            }
        }
        wb.write(output);
        output.close();
    }

}
