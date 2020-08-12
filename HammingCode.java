package assignments.assignment1;

//mengimpor module yang dibutuhkan untuk menerima input
import java.util.Scanner;

//class yang berisi method-method untuk mengerjakan persoalan Hamming Code
public class HammingCode {

    //mendeklarasi variabel yang menandakan pilihan user
    static final int ENCODE_NUM = 1;
    static final int DECODE_NUM = 2;
    static final int EXIT_NUM = 3;

    /**
     * Sebuah method yang akan mengencode data yang dimasukkan.
     * @param data -> data masukan user yang akan diencode
     * @return data -> data masukan user yang telah diencode
     */
    public static String encode(String data) {
        //menginisialisasi variabel yang akan digunakan
        int jumlahRedundan;
        int panjangBit = data.length();

        //mencari jumlah parity berdasarkan panjang data
        for (jumlahRedundan = 1; jumlahRedundan > 0; jumlahRedundan++) {
            if (Math.pow(2, jumlahRedundan) >= (panjangBit + jumlahRedundan + 1)) {
                break;
            }
        }

        //menyisipkan parity sesuai tempatnya pada data tersebut
        for (int urutanRedundan = 0; urutanRedundan < jumlahRedundan; urutanRedundan++) {
            data = data.substring(0, (int) Math.pow(2, urutanRedundan) - 1) + "_"
                    + data.substring((int) Math.pow(2, urutanRedundan) - 1);
        }

        //memberi nilai pada setiap redundan dengan memanggil method cariNilaiRedundan
        for (int hitungRedundan = 0; hitungRedundan < jumlahRedundan; hitungRedundan++) {
            data = cariNilaiRedundan(data, (int) Math.pow(2, hitungRedundan));
        }

        //memberi nilai balik berupa data yang telah diencode
        return data;
    }

    /**
     * Sebuah method yang memberi nilai pada masing-masing paritynya.
     * @param data -> data yang akan diubah di posisi redundannya
     * @param posisiRedundan -> posisi bit yang ingin diberi nilai redundannya
     * @return data yang sudah diubah pada posisi redundannya
     */
    public static String cariNilaiRedundan(String data, int posisiRedundan) {
        //membuat string kosong dan menginisialisasi panjang data
        String dataData = "";
        int totalBit = data.length();

        //membuat string sesuai dengan pola parity yang di cek
        for (int indeks = posisiRedundan - 1; indeks < totalBit; indeks += 2 * posisiRedundan) {
            //membuat kasus jika longkapan melebihi panjang data
            if (indeks + posisiRedundan > data.length()) {
                dataData += data.substring(indeks);
            } else {
                dataData += data.substring(indeks, indeks + posisiRedundan);
            }
        }

        //menghitung jumlah angka 1 pada string dataData
        int jumlahSatu = 0;
        for (int indeks = 0; indeks < dataData.length(); indeks++) {
            if (dataData.charAt(indeks) == '1') {
                jumlahSatu += 1;
            }
        }

        //mengembalikan data asli dengan nilai redundannya pada posisi yang diperiksa
        return data.substring(0, posisiRedundan - 1) + (jumlahSatu % 2)
                + data.substring(posisiRedundan);
    }

    /**
     * Sebuah method yang akan mendecode data yang dimasukkan.
     * @param data -> data masukan user yang akan didecode
     * @return data -> data masukan user yang telah didecode
     */
    public static String decode(String data) {
        //menginisialisasi variabel yang akan digunakan
        int jumlahRedundan;
        int panjangBit = data.length();

        //mencari jumlah parity sesuai panjang data yang sudah tergabung dengan paritynya
        for (jumlahRedundan = 1; jumlahRedundan > 0; jumlahRedundan++) {
            if (Math.pow(2, jumlahRedundan) >= (panjangBit + 1)) {
                break;
            }
        }

        //mencari pada bit ke berapa terdapat kesalahan
        //dilakukan dengan memanggil method cariPolaBitSalah
        int bitKe = 0;
        for (int cekRedundan = 0; cekRedundan < jumlahRedundan; cekRedundan++) {
            bitKe += cariPolaBitSalah(data, (int) Math.pow(2, cekRedundan));
        }

        //jika hasilnya pada bit ke-0 akan diubah ke-1
        //hal ini untuk mencegah eksepsi pada lanjutan program ini
        bitKe = (bitKe == 0 ? 1 : bitKe);

        //mengubah bit yang salah
        data = data.substring(0, bitKe - 1) + (data.charAt(bitKe - 1) == '0' ? 1 : 0)
                + data.substring(bitKe);

        //menghilangkan nilai redundan pada posisi parity masing-masing
        for (int urutanRedundan = 0; urutanRedundan < jumlahRedundan; urutanRedundan++) {
            data = data.substring(0, (int) Math.pow(2, urutanRedundan) - urutanRedundan - 1)
                    + data.substring((int) Math.pow(2, urutanRedundan) - urutanRedundan);
        }

        //mengembalikan data yang telah didecode
        return data;
    }

    /**
     * Sebuah method yang mengecek setiap polabit.
     * @param data -> data yang user masukan untuk diolah
     * @param posisiRedundan -> yang akan menentukan start pengecekan dan polanya
     * @return nilai berupa posisi parity jika pada pola ada yang salah atau
     *         nilai berupa 0 jika tak ada yang salah
     */
    public static int cariPolaBitSalah(String data, int posisiRedundan) {
        //membuat string kosoong dan variabel yang akan digunakan
        String dataData = "";
        int totalBit = data.length();

        //membuat string dengan pola parity yang di cek
        for (int indeks = posisiRedundan - 1; indeks < totalBit; indeks += 2 * posisiRedundan) {
            //membuat kasus jika longkapan melebihi panjang bit
            if (indeks + posisiRedundan > totalBit) {
                dataData += data.substring(indeks);
            } else {
                dataData += data.substring(indeks, indeks + posisiRedundan);
            }
        }

        //menghitung jumlah satu dari string sesuai pola parity yang dibuat sebelumnya
        int jumlahSatu = 0;
        for (int indeks = 0; indeks < dataData.length(); indeks++) {
            if (dataData.charAt(indeks) == '1') {
                jumlahSatu += 1;
            }
        }

        //mengembalikan nilai posisi redundan jika hasilnya tidak genap
        //sebaliknya akan mengembalikan nilai nol jika hasilnya genap
        return (jumlahSatu % 2 != 0 ? posisiRedundan : 0);
    }


    /**
     * Sebuah method utama yang akan menjadi sentral dalam jalannya program ini.
     * @param args -> array dari kumpulan string (sering dijadikan default param in main method)
     */
    public static void main(String[] args) {

        //membuat ucapan selamat datang dan menginstansiasi objek agar bisa menerima input
        System.out.println("Selamat datang di program Hamming Code!");
        System.out.println("=======================================");
        Scanner in = new Scanner(System.in);

        //membuat default variabel dan melooping program sampai default variabel dirubah
        boolean hasChosenExit = false;
        while (!hasChosenExit) {

            //memberikan pilihan operasi dan meminta input dari user atas pilihannya
            System.out.println();
            System.out.println("Pilih operasi:");
            System.out.println("1. Encode");
            System.out.println("2. Decode");
            System.out.println("3. Exit");
            System.out.print("Masukkan nomor operasi yang diinginkan: ");
            int operation = in.nextInt();

            if (operation == ENCODE_NUM) {
                /*
                meminta data yang ingin diubah jika memilih encode
                menjalankan fungsi encode
                menampilkan hasil yang telah di encode kepada user
                */
                System.out.print("Masukkan data: ");
                String data = in.next();
                String code = encode(data);
                System.out.println("Code dari data tersebut adalah: " + code);
            } else if (operation == DECODE_NUM) {
                /*
                meminta data yang ingin diubah jika memilih decode
                menjalankan fungsi decode
                menampilkan hasil yang telah di decode kepada user
                */
                System.out.print("Masukkan code: ");
                String code = in.next();
                String data = decode(code);
                System.out.println("Data dari code tersebut adalah: " + data);
            } else if (operation == EXIT_NUM) {
                //mengakhiri program jika user memilih exit
                System.out.println("Sampai jumpa!");
                hasChosenExit = true;
            }
        }

        //menutup kembali input
        in.close();
    }
}