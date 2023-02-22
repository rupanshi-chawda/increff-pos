<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21.0cm" margin-top="1cm" margin-left="2cm" margin-right="2cm" margin-bottom="1cm">
                    <!-- Page template goes here -->
                    <fo:region-body />
                    <fo:region-before region-name="xsl-region-before" extent="3cm"/>
                    <fo:region-after region-name="xsl-region-after" extent="4cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <!-- Page content goes here -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block>
                        <fo:table>

                            <fo:table-column column-width="8.5cm"/>
                            <fo:table-column column-width="8.5cm"/>
                            <fo:table-body>
                                <fo:table-row line-height="20pt" color="#293459">
                                    <fo:table-cell>
                                        <fo:block text-align="left" padding-left="5pt" padding-top="10pt" font-size="20pt">
                                            Tax Invoice
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block text-align="right" padding-right="5pt">
                                            <fo:external-graphic src="data:image/png;base64,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"  content-height="scale-to-fit" height="35" content-width="100" scaling="non-uniform"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell font-size="8pt" padding-top="5pt">
                                        <fo:block text-align="left" padding-left="5pt">
                                            Sector 6, HSR Layout&#x2028;
                                            Bengaluru, Karnataka 560102
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body" line-height="20pt">
                    <xsl:apply-templates />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="invoice">
        <fo:block></fo:block>
        <fo:block space-before="140pt" width="17cm" >
            <fo:table>
                <fo:table-column column-width="5.5cm"/>
                <fo:table-column column-width="5.5cm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block text-align="left" color="#293459">
                                <fo:inline font-weight="bold">Order ID - </fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Order Date - </fo:inline>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <xsl:value-of select="./order_id"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./order_date"></xsl:value-of>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <fo:block space-before="30pt">
            <fo:table line-height="30px">
                <fo:table-column column-width="2cm"/>
                <fo:table-column column-width="5cm"/>
                <fo:table-column column-width="2.5cm"/>
                <fo:table-column column-width="4cm"/>
                <fo:table-column column-width="4cm"/>
                <fo:table-header>
                    <fo:table-row background-color="#293459" text-align="center" font-weight="bold" color="white">
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Item ID</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Name</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Quantity</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Selling Price (per unit)</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Total</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <xsl:apply-templates select="order_item"></xsl:apply-templates>


                    <fo:table-row font-weight="bold">
                        <fo:table-cell number-columns-spanned="4" text-align="right" padding-right="3pt">
                            <fo:block>Total</fo:block>
                        </fo:table-cell>
                        <fo:table-cell  text-align="right" padding-right="3pt" border="1px solid #b8b6b6">
                            <fo:block>
                                <xsl:value-of select="amount" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

    <xsl:template match="order_item">
        <fo:table-row>
            <fo:table-cell border="1px solid #b8b6b6" padding-left="3pt">
                <fo:block>
                    <xsl:value-of select="id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" padding-left="3pt">
                <fo:block>
                    <xsl:value-of select="product_name"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell border="1px solid #b8b6b6" text-align="center">
                <fo:block>
                    <xsl:value-of select="quantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center">
                <fo:block>
                    <xsl:value-of select="selling_price"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="multiplied"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>

    </xsl:template>
</xsl:stylesheet>